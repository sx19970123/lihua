package com.lihua.system.service.impl;

import ch.qos.logback.core.testUtil.MockInitialContext;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.exception.ServiceException;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysAttachment;
import com.lihua.system.mapper.SysAttachmentMapper;
import com.lihua.system.model.dto.SysAttachmentDTO;
import com.lihua.system.service.SysAttachmentService;
import com.lihua.system.strategy.AttachmentStorageStrategy;
import com.lihua.utils.crypt.AesUtils;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.file.FileUtils;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
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

    // 存储路径
    @Value("${lihua.uploadFilePath}")
    private String uploadFilePath;

    // 附件存储模式
    @Value("${lihua.uploadFileModel}")
    private String uploadFileModel;

    // 文件下载链接有效期
    @Value("${lihua.fileDownloadExpireTime}")
    private String fileDownloadExpireTime;

    // 公开文件的业务编码，publicLocalDownload 中判断包含业务编码的文件才会进行返回
    private final List<String> publicBusinessCodeList = List.of("UserAvatar", "SystemNotice");

    @Override
    public IPage<SysAttachment> queryPage(SysAttachmentDTO sysAttachmentDTO) {
        return null;
    }

    @Override
    public SysAttachment queryById(String id) {
        return null;
    }

    @Override
    public boolean existsAttachmentByMd5(String md5, String originFileName) {
        AttachmentStorageStrategy strategy = getStrategy();
        // 根据md5和原文件名查询是否存在
        LambdaQueryChainWrapper<SysAttachment> wrapper = lambdaQuery().eq(SysAttachment::getMd5, md5).eq(SysAttachment::getOriginalName, originFileName);
        SysAttachment attachment = queryOne(wrapper);
        if (attachment == null || !StringUtils.hasText(attachment.getPath())) {
            return false;
        }

        // 检查服务器文件是否存在
        boolean exists = strategy.isExists(attachment.getPath());

        if (exists) {
            return true;
        }
        throw new FileException("服务器文件与数据库记录不符，服务器无该文件");
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
        // 去重查询path和原文件名
        List<SysAttachment> sysAttachmentList = lambdaQuery()
                .select(SysAttachment::getId, SysAttachment::getPath, SysAttachment::getOriginalName)
                .in(SysAttachment::getId, ids)
                .eq(SysAttachment::getStatus, "0")
                .list();

        // 获取未查询出结果的数据集
        List<String> dbIds = sysAttachmentList.stream().map(SysAttachment::getId).toList();
        ids.removeAll(dbIds);

        // 获取文件访问路径
        sysAttachmentList.forEach(sysAttachment -> sysAttachment.setPath(getDownloadURL(sysAttachment.getId())));

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
                .setStorageLocation(uploadFileModel)
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
                .setStorageLocation(uploadFileModel)
                .setCreateId(LoginUserContext.getUserId())
                .setCreateTime(DateUtils.now())
                .setDelFlag("0"));
        saveBatch(sysAttachmentList);

        return sysAttachmentList.stream().map(SysAttachment::getId).toList();
    }

    @Override
    public String upload(MultipartFile file) {
        AttachmentStorageStrategy strategy = getStrategy();
        return strategy.uploadFile(file);
    }

    @Override
    public String urlUpload(String url) {
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(uploadFileModel);
        return attachmentStorageStrategy.uploadFile(url);
    }

    @Override
    public List<Integer> chunksUploadedIndex(String md5) {
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(uploadFileModel);
        List<Integer> tempChunksIndexList = attachmentStorageStrategy.getTempChunksIndex(md5);
        return tempChunksIndexList == null ? new ArrayList<>() : tempChunksIndexList;
    }

    @Override
    public void chunksUpload(MultipartFile file, String uploadId, String index) {
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(uploadFileModel);
        attachmentStorageStrategy.uploadTempFile(file, Path.of(uploadFilePath,"temporary", uploadId, index).toString());
    }

    @Override
    public String chunksMerge(SysAttachment sysAttachment, String uploadId, Integer total) {
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(uploadFileModel);
        return attachmentStorageStrategy.chunksMerge(sysAttachment.getOriginalName(), sysAttachment.getMd5(), uploadId, total);
    }

    @Override
    public String queryOriginFileName(String path) {
        SysAttachment sysAttachment = queryOne(lambdaQuery().eq(SysAttachment::getPath, path));
        if (sysAttachment != null) {
            return sysAttachment.getOriginalName();
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteByIds(List<String> ids) {
        AttachmentStorageStrategy strategy = getStrategy();
        // 根据ids获取可删除文件的路径
        List<String> deletablePathList = sysAttachmentMapper.queryDeletablePathByIds(ids);
        // 删除数据库记录
        removeByIds(ids);

        if (!deletablePathList.isEmpty()) {
            // 删除服务器文件
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
    @Transactional
    public void deleteByPath(String path) {
        AttachmentStorageStrategy strategy = getStrategy();
        // 删除服务器文件
        strategy.delete(path);

        // 删除数据库数据
        QueryWrapper<SysAttachment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysAttachment::getPath, path);
        remove(queryWrapper);
    }

    @Override
    public String getDownloadURL(String id) {
        AttachmentStorageStrategy strategy = getStrategy();
        SysAttachment sysAttachment = lambdaQuery()
                .select(SysAttachment::getPath)
                .eq(SysAttachment::getId, id)
                .eq(SysAttachment::getStatus, "0")
                .isNotNull(SysAttachment::getPath)
                .one();

        if (sysAttachment == null) {
            return null;
        }

        return strategy.getDownloadURL(sysAttachment.getPath(), Long.parseLong(fileDownloadExpireTime));
    }

    @Override
    public List<String> getDownloadURL(List<String> pathList) {
        return List.of();
    }

    @Override
    public File localDownload(String key) {
        if (!"LOCAL".equals(uploadFileModel)) {
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

        // 文件路径::过期时间
        String[] splitParams = params.split("::");

        // 过期时间
        String expirationTime = splitParams[1];

        // 当前时间戳大于过期时间，表示链接已过期
        if (DateUtils.nowTimeStamp() > Long.parseLong(expirationTime)) {
            throw new FileException("当前链接已失效");
        }

        // 获取原文件名
        return new File(splitParams[0]);
    }

    @Override
    public ResponseEntity<StreamingResponseBody> publicDownload(String id) {
        // 根据路径和公开业务编码进行查询
        SysAttachment sysAttachment = lambdaQuery()
                .select(SysAttachment::getPath, SysAttachment::getOriginalName)
                .eq(SysAttachment::getId, id)
                .in(SysAttachment::getBusinessCode, publicBusinessCodeList)
                .one();

        if (sysAttachment == null) {
            return null;
        }

        AttachmentStorageStrategy strategy = getStrategy();
        File download = strategy.download(sysAttachment.getPath());

        // 返回下载file和原文件名
        return BaseController.success(download, sysAttachment.getOriginalName());
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
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(uploadFileModel);
        if (attachmentStorageStrategy == null) {
            log.error("获取附件实现策略失败，请检查uploadFileModel策略配置，可选参数" + attachmentStorageStrategyMap.keySet());
            throw new ServiceException("获取附件实现策略失败");
        }
        return attachmentStorageStrategy;
    }
}
