package com.lihua.service.system.attachment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.exception.ServiceException;
import com.lihua.model.web.BaseController;
import com.lihua.entity.system.SysAttachment;
import com.lihua.mapper.system.SysAttachmentMapper;
import com.lihua.model.system.dto.SysAttachmentDTO;
import com.lihua.model.system.vo.SysAttachmentVO;
import com.lihua.strategy.AttachmentStorageStrategy;
import com.lihua.utils.crypt.AesUtils;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.file.FileUtils;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment> implements SysAttachmentService {

    // 不同附件存储方法实现策略
    @Resource
    private Map<String, AttachmentStorageStrategy> attachmentStorageStrategyMap;

    @Resource
    private SysAttachmentMapper sysAttachmentMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private LihuaConfig lihuaConfig;

    @Override
    public IPage<SysAttachment> queryPage(SysAttachmentDTO sysAttachmentDTO) {
        IPage<SysAttachment> iPage = new Page<>(sysAttachmentDTO.getPageNum(), sysAttachmentDTO.getPageSize());
        QueryWrapper<SysAttachment> queryWrapper = new QueryWrapper<>();

        //  原附件名
        if (StringUtils.hasText(sysAttachmentDTO.getOriginalName())) {
            queryWrapper.lambda().like(SysAttachment::getOriginalName, sysAttachmentDTO.getOriginalName());
        }
        //  状态
        if (StringUtils.hasText(sysAttachmentDTO.getStatus())) {
            queryWrapper.lambda().eq(SysAttachment::getStatus, sysAttachmentDTO.getStatus());
        }
        //  业务名称
        if (StringUtils.hasText(sysAttachmentDTO.getBusinessName())) {
            queryWrapper.lambda().like(SysAttachment::getBusinessName, sysAttachmentDTO.getBusinessName());
        }
        //  上传时间
        List<LocalDateTime> createTimeList = sysAttachmentDTO.getCreateTimeList();
        if (createTimeList != null && createTimeList.size() == 2) {
            queryWrapper.lambda().between(SysAttachment::getCreateTime, createTimeList.get(0), createTimeList.get(1));
        }
        queryWrapper.lambda().orderByDesc(SysAttachment::getCreateTime);
        sysAttachmentMapper.selectPage(iPage, queryWrapper);
        return iPage;
    }

    @Override
    public SysAttachmentVO queryById(String id) {
        return sysAttachmentMapper.queryById(id);
    }

    @Override
    public boolean existsAttachmentByMd5(String md5, String originFileName) {
        AttachmentStorageStrategy strategy = getStrategy();
        // 根据md5和原附件名查询是否存在
        LambdaQueryChainWrapper<SysAttachment> wrapper = lambdaQuery().eq(SysAttachment::getMd5, md5).eq(SysAttachment::getOriginalName, originFileName);
        SysAttachment attachment = queryOne(wrapper);
        if (attachment == null || !StringUtils.hasText(attachment.getPath())) {
            return false;
        }

        // 检查服务器附件是否存在
        boolean exists = strategy.isExists(attachment.getPath());

        if (exists) {
            return true;
        }
        throw new FileException("服务器附件与数据库记录不符，服务器无该附件");
    }

    @Override
    public String fastUpload(SysAttachment sysAttachment) {
        SysAttachment attachment = queryOne(lambdaQuery().eq(SysAttachment::getMd5, sysAttachment.getMd5()));
        if (attachment == null) {
            return null;
        }
        // 获取路径
        String path = attachment.getPath();
        sysAttachment
                .setPath(path)
                .setStatus("0");
        // 插入新数据
        saveAttachment(sysAttachment);
        return sysAttachment.getId();
    }

    @Override
    public List<SysAttachment> queryAttachmentInfoByIds(List<String> ids) {
        // 去重查询path和原附件名
        List<SysAttachment> sysAttachmentList = lambdaQuery()
                .select(SysAttachment::getId, SysAttachment::getPath, SysAttachment::getOriginalName)
                .in(SysAttachment::getId, ids)
                .eq(SysAttachment::getStatus, "0")
                .list();

        // 获取未查询出结果的数据集
        List<String> dbIds = sysAttachmentList.stream().map(SysAttachment::getId).toList();
        ids.removeAll(dbIds);

        // 获取附件访问路径
        sysAttachmentList.forEach(sysAttachment -> sysAttachment.setPath(getDownloadURL(sysAttachment.getId(), null)));

        // 未查询出结果的数据集创建对象
        ids.forEach(id -> {
            SysAttachment attachment = new SysAttachment();
            attachment.setId(id).setOriginalName("附件丢失（附件id：" + id + "）").setStatus("error");
            sysAttachmentList.add(attachment);
        });

        return sysAttachmentList;
    }

    @Override
    public String saveAttachment(SysAttachment sysAttachment) {
        sysAttachment
                .setStorageName(FileUtils.getFileNameByPath(sysAttachment.getPath()))
                .setExtensionName(FileUtils.getExtensionNameByFileName(sysAttachment.getStorageName()))
                .setStorageLocation(lihuaConfig.getUploadFileModel())
                .setCreateId(LoginUserContext.getUserId())
                .setCreateTime(DateUtils.now())
                .setDelFlag("0");
        // 保存附件信息
        saveOrUpdate(sysAttachment);
        return sysAttachment.getId();
    }

    @Override
    @Transactional
    public List<String> batchSaveAttachment(List<SysAttachment> sysAttachmentList) {
        sysAttachmentList.forEach(sysAttachment -> sysAttachment
                .setStorageName(FileUtils.getFileNameByPath(sysAttachment.getPath()))
                .setExtensionName(FileUtils.getExtensionNameByFileName(sysAttachment.getStorageName()))
                .setStorageLocation(lihuaConfig.getUploadFileModel())
                .setCreateId(LoginUserContext.getUserId())
                .setCreateTime(DateUtils.now())
                .setDelFlag("0"));
        saveBatch(sysAttachmentList);
        return sysAttachmentList.stream().map(SysAttachment::getId).toList();
    }

    @Override
    public String upload(MultipartFile file) {
        AttachmentStorageStrategy strategy = getStrategy();
        // 获取新的附件名称
        String uuidFileName = FileUtils.generateUUIDFileName(file.getOriginalFilename());
        // 通过指定路径拼接附件全路径
        String fullFilePath = lihuaConfig.getUploadFilePath() + uuidFileName;
        // 附件上传
        strategy.uploadFile(file, fullFilePath);
        return fullFilePath;
    }

    @Override
    public List<Integer> chunksUploadedIndex(String uploadId) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();

        List<Integer> tempChunksIndexList = attachmentStorageStrategy.getUploadedChunksIndex(getChunksFullPathByUploadId(uploadId), uploadId);
        return tempChunksIndexList == null ? new ArrayList<>() : tempChunksIndexList;
    }

    @Override
    public String chunksGetUploadId(String fullFilePath) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();
        String uploadId = attachmentStorageStrategy.getUploadId(fullFilePath);
        // uploadId和fullFilePath保存到redis
        redisCache.setCacheObject(uploadId, fullFilePath);
        return uploadId;
    }

    @Override
    public void chunksUpload(MultipartFile file, String uploadId, String index) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();
        attachmentStorageStrategy.chunksUploadFile(file, getChunksFullPathByUploadId(uploadId), Integer.parseInt(index), uploadId);
    }

    @Override
    public void chunksMerge(String md5, String uploadId, Integer total) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();
        String fullFilePath = getChunksFullPathByUploadId(uploadId);
        attachmentStorageStrategy.chunksMerge(fullFilePath, md5, uploadId, total);
    }

    @Override
    @Transactional
    public void deleteByIds(List<String> ids) {
        AttachmentStorageStrategy strategy = getStrategy();
        Long count = lambdaQuery()
                .or(wrapper -> wrapper.eq(SysAttachment::getStatus, "1").or().eq(SysAttachment::getStatus, "3"))
                .in(SysAttachment::getId, ids)
                .count();

        if (count != ids.size()) {
            throw new ServiceException("上传成功、分片上传中状态附件不允许删除");
        }

        // 根据ids获取可删除附件的路径
        List<String> deletablePathList = sysAttachmentMapper.queryDeletablePathByIds(ids);
        // 删除数据库记录
        removeByIds(ids);

        if (!deletablePathList.isEmpty()) {
            // 删除服务器附件
            deletablePathList.forEach(strategy::delete);
        }
    }

    @Override
    public void deleteFromBusiness(String id) {
        UpdateWrapper<SysAttachment> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .lambda()
                .set(SysAttachment::getStatus, "3")
                .eq(SysAttachment::getId, id);
        update(updateWrapper);
    }

    @Override
    public String getDownloadURL(String id, Integer expireTime) {
        AttachmentStorageStrategy strategy = getStrategy();
        SysAttachment sysAttachment = lambdaQuery()
                .select(SysAttachment::getPath, SysAttachment::getOriginalName)
                .eq(SysAttachment::getId, id)
                .eq(SysAttachment::getStatus, "0")
                .isNotNull(SysAttachment::getPath)
                .one();

        if (sysAttachment == null) {
            return null;
        }

        return strategy.getDownloadURL(sysAttachment.getPath(), sysAttachment.getOriginalName(), expireTime != null && expireTime != 0 ? expireTime : lihuaConfig.getFileDownloadExpireTime());
    }

    @Override
    public File localDownload(String key) {
        if (!"LOCAL".equals(lihuaConfig.getUploadFileModel())) {
            throw new FileException("存储模式不受支持");
        }
        String params;
        try {
            // 解密数据
            String decode = URLDecoder.decode(URLEncoder.encode(key, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            params = AesUtils.decryptToString(decode, SysBaseEnum.ATTACHMENT_URL_KEY.getValue());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException();
        }

        // 附件路径::过期时间
        String[] splitParams = params.split("::");

        // 过期时间
        String expirationTime = splitParams[1];

        // 当前时间戳大于过期时间，表示链接已过期
        if (DateUtils.nowTimeStamp() > Long.parseLong(expirationTime)) {
            throw new FileException("当前链接已失效");
        }

        // 获取原附件名
        return new File(splitParams[0]);
    }

    @Override
    public ResponseEntity<StreamingResponseBody> publicDownload(String id, String fileName) {
        // 根据路径和公开业务编码进行查询
        SysAttachment sysAttachment = lambdaQuery()
                .select(SysAttachment::getPath, SysAttachment::getOriginalName, SysAttachment::getSize)
                .eq(SysAttachment::getId, id)
                .in(SysAttachment::getBusinessCode, lihuaConfig.getUploadPublicBusinessCode())
                .one();

        if (sysAttachment == null) {
            throw new FileException("请求资源不存在");
        }

        AttachmentStorageStrategy strategy = getStrategy();
        InputStream inputStream = strategy.download(sysAttachment.getPath());
        // 返回下载file和原附件名
        return BaseController.success(inputStream, sysAttachment.getOriginalName(), sysAttachment.getSize());
    }

    // 分片上传中通过uploadId获取fullFilePath
    private String getChunksFullPathByUploadId(String uploadId) {
        // 通过uploadId获取fullFilePath
        String fullFilePath = redisCache.getCacheObject(uploadId, String.class);
        if (!StringUtils.hasText(fullFilePath)) {
            List<SysAttachment> list = lambdaQuery().select(SysAttachment::getPath).eq(SysAttachment::getUploadId, uploadId).list();
            if (list.isEmpty()) {
                throw new FileException("获取分片路径失败");
            }
            fullFilePath = list.get(0).getPath();
        }

        return fullFilePath;
    }

    // 根据需要条件查询单条数据
    private SysAttachment queryOne(LambdaQueryChainWrapper<SysAttachment> chainWrapper) {
        List<SysAttachment> list = chainWrapper
                .eq(SysAttachment::getDelFlag, "0")
                .eq(SysAttachment::getStatus, "0")
                .list();

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    // 获取 AttachmentStorageStrategy 对应实现
    private AttachmentStorageStrategy getStrategy() {
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(lihuaConfig.getUploadFileModel());
        if (attachmentStorageStrategy == null) {
            log.error("获取附件实现策略失败，请检查uploadFileModel策略配置，可选参数" + attachmentStorageStrategyMap.keySet());
            throw new ServiceException("获取附件实现策略失败");
        }
        return attachmentStorageStrategy;
    }
}
