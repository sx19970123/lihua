package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.attachment.config.AttachmentConfig;
import com.lihua.attachment.enums.AttachmentEnum;
import com.lihua.attachment.exception.AttachmentException;
import com.lihua.attachment.model.AttachmentResponse;
import com.lihua.attachment.strategy.AttachmentStorageStrategy;
import com.lihua.attachment.utils.FileUtils;
import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.crypt.AesUtils;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.SysAttachment;
import com.lihua.mapper.SysAttachmentMapper;
import com.lihua.model.vo.SysAttachmentChunkVO;
import com.lihua.redis.cache.RedisCache;
import com.lihua.redis.enums.RedisKeyPrefixEnum;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.service.SysAttachmentStorageService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysAttachmentStorageServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment> implements SysAttachmentStorageService {

    // 不同附件存储方法实现策略
    @Resource
    private Map<String, AttachmentStorageStrategy> attachmentStorageStrategyMap;

    @Resource
    private RedisCache redisCache;

    @Resource
    private AttachmentConfig attachmentConfig;

    @Resource
    private SysAttachmentMapper sysAttachmentMapper;

    @Override
    public boolean existsAttachmentByMd5(String md5, String originFileName) {
        AttachmentStorageStrategy strategy = getStrategy();
        // 根据md5查询附件是否存在
        LambdaQueryChainWrapper<SysAttachment> wrapper =
                lambdaQuery()
                .eq(SysAttachment::getMd5, md5);
        // 根据附件原名查询是否存在
        if (StringUtils.hasText(originFileName)) {
            wrapper.eq(SysAttachment::getOriginalName, originFileName);
        }
        SysAttachment attachment = queryOne(wrapper);
        if (attachment == null || !StringUtils.hasText(attachment.getPath())) {
            return false;
        }
        // 检查服务器附件是否存在
        boolean exists = strategy.isExists(attachment.getPath());
        if (exists) {
            return true;
        }
        throw new AttachmentException("服务器附件与数据库记录不符，服务器无该附件");
    }

    @Override
    public List<SysAttachment> queryAttachmentInfoByIds(List<String> ids) {
        // 去重查询path和原附件名
        List<SysAttachment> sysAttachmentList = lambdaQuery()
                .select(SysAttachment::getId, SysAttachment::getPath, SysAttachment::getOriginalName, SysAttachment::getType)
                .in(SysAttachment::getId, ids)
                .eq(SysAttachment::getStatus, "0")
                .list();

        // 获取未查询出结果的数据集
        List<String> dbIds = sysAttachmentList.stream().map(SysAttachment::getId).toList();
        ids.removeAll(dbIds);

        // 获取附件访问路径
        sysAttachmentList.forEach(sysAttachment -> sysAttachment.setPath(getAttachmentURL(sysAttachment.getPath(), sysAttachment.getOriginalName(), null)));

        // 未查询出结果的数据集创建对象
        ids.forEach(id -> {
            SysAttachment attachment = new SysAttachment();
            String errMsg = "附件丢失（附件id：" + id + "）";
            attachment.setId(id).setOriginalName(errMsg).setStatus("error").setErrorMsg(errMsg);
            sysAttachmentList.add(attachment);
        });

        return sysAttachmentList;
    }

    @Override
    public String uploadAttachment(MultipartFile file, SysAttachment sysAttachment) {
        try {
            fillParameters(file, sysAttachment);
            String path = upload(file, sysAttachment.getBusinessCode());
            sysAttachment.setPath(path).setStatus("0");
            return saveAttachment(sysAttachment);
        } catch (Exception e) {
            sysAttachment.setStatus("1").setErrorMsg(e.getMessage());
            saveAttachment(sysAttachment);
            throw new AttachmentException("附件上传失败");
        }
    }

    @Override
    public String publicUpload(MultipartFile file, String businessCode) {
        boolean contains = attachmentConfig.getUploadPublicBusinessCode().contains(businessCode);
        if (!contains) {
            log.error("请检查配置文件，是否将此业务编码 " + businessCode + " 配置在 uploadPublicBusinessCode");
            throw new AttachmentException("当前附件业务编码不为公开访问附件，无法上传");
        }

        return upload(file, businessCode);
    }

    @Override
    public String fastUpload(SysAttachment sysAttachment) {
        SysAttachment attachment = queryOne(lambdaQuery().eq(SysAttachment::getMd5, sysAttachment.getMd5()));
        if (attachment == null) {
            return null;
        }
        // 完善其他字段
        String path = attachment.getPath();
        sysAttachment
                .setPath(path)
                .setType(attachment.getType())
                .setStatus("0");
        // 插入新数据
        saveAttachment(sysAttachment);
        return sysAttachment.getId();
    }

    @Override
    public SysAttachmentChunkVO chunksUploadAttachmentStart(SysAttachment sysAttachment) {
        String path = Paths.get(
                attachmentConfig.getUploadFilePath(),
                sysAttachment.getBusinessCode(),
                FileUtils.generateUUIDFileName(sysAttachment.getOriginalName())
        ).toString();
        path = path.replace("\\", "/");
        sysAttachment.setStatus("2").setPath(path);
        try {
            // 获取附件id
            String uploadId = chunksGetUploadId(path);
            sysAttachment.setUploadId(uploadId);
            // 保存附件信息
            String attachmentId = saveAttachment(sysAttachment);
            return new SysAttachmentChunkVO(uploadId, attachmentId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sysAttachment.setStatus("1").setErrorMsg(e.getMessage());
            saveAttachment(sysAttachment);
            throw new AttachmentException(e.getMessage());
        }
    }

    @Override
    public List<Integer> chunksUploadedIndex(String uploadId) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();

        List<Integer> tempChunksIndexList = attachmentStorageStrategy.getUploadedChunksIndex(getChunksFullPathByUploadId(uploadId), uploadId);
        return tempChunksIndexList == null ? new ArrayList<>() : tempChunksIndexList;
    }

    @Override
    public void chunksUpload(MultipartFile file, String uploadId, String index) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();
        attachmentStorageStrategy.chunksUploadFile(file, getChunksFullPathByUploadId(uploadId), Integer.parseInt(index), uploadId);
    }

    @Override
    public String chunksMerge(SysAttachment sysAttachment, Integer total) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();
        String uploadId = sysAttachment.getUploadId();
        try {
            String fullFilePath = getChunksFullPathByUploadId(uploadId);
            // 分片合并
            attachmentStorageStrategy.chunksMerge(fullFilePath, sysAttachment.getMd5(), uploadId, total);
            sysAttachment.setStatus("0");
            return saveAttachment(sysAttachment);
        } catch (Exception e) {
            sysAttachment.setStatus("1").setErrorMsg(e.getMessage());
            saveAttachment(sysAttachment);
            throw new AttachmentException("附件合并失败");
        } finally {
            // 删除redis缓存
            redisCache.delete(RedisKeyPrefixEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX + uploadId);
        }
    }

    @Override
    public void deleteFromBusiness(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        UpdateWrapper<SysAttachment> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .lambda()
                .set(SysAttachment::getStatus, "3")
                .in(SysAttachment::getId, ids);
        update(updateWrapper);
    }

    @Override
    public void deleteFiles(List<String> fullFilePathList) {
        if (fullFilePathList == null) {
            return;
        }

        AttachmentStorageStrategy strategy = getStrategy();
        fullFilePathList.forEach(strategy::delete);
    }

    @Override
    public String getAttachmentURL(String path, String originalName, Integer expireTime) {
        AttachmentStorageStrategy strategy = getStrategy();
        return strategy.getDownloadURL(path, originalName, expireTime != null && expireTime != 0 ? expireTime : attachmentConfig.getFileDownloadExpireTime());
    }


    @Override
    public ResponseEntity<StreamingResponseBody> localDownload(String key, String originName) {
        if (!"LOCAL".equals(attachmentConfig.getUploadFileModel())) {
            throw new AttachmentException("存储模式不受支持");
        }
        String params;
        try {
            // 解密数据
            String decode = URLDecoder.decode(URLEncoder.encode(key, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            params = AesUtils.decryptToString(decode, AttachmentEnum.ATTACHMENT_URL_KEY.getValue());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AttachmentException();
        }

        // 附件路径::过期时间
        String[] splitParams = params.split("::");

        // 过期时间
        String expirationTime = splitParams[1];

        // 当前时间戳大于过期时间，表示链接已过期
        if (DateUtils.nowTimeStamp() > Long.parseLong(expirationTime)) {
            throw new AttachmentException("当前链接已失效");
        }

        String filePath = splitParams[0];

        // 校验路径
        if (FileUtils.checkPath(filePath, attachmentConfig.getUploadFilePath())) {
            return AttachmentResponse.success(new File(filePath), originName);
        }

        throw new AttachmentException("下载失败，路径不匹配");
    }

    @Override
    public ResponseEntity<StreamingResponseBody> download(String fullPath) {
        List<String> uploadPublicBusinessCode = attachmentConfig.getUploadPublicBusinessCode();
        String uploadFilePath = attachmentConfig.getUploadFilePath();

        Path targetPath = Paths.get(fullPath).normalize();

        // 校验路径
        boolean allow = uploadPublicBusinessCode.stream()
                .map(code -> Paths.get(uploadFilePath, code).normalize())
                .anyMatch(targetPath::startsWith);

        if (!allow) {
            throw new AttachmentException("未知的附件");
        }

        AttachmentStorageStrategy strategy = getStrategy();
        return AttachmentResponse.success(strategy.download(fullPath), FileUtils.getFileNameByPath(fullPath));
    }

    // 附件上传方法
    private String upload(MultipartFile file, String businessCode) {
        AttachmentStorageStrategy strategy = getStrategy();
        // 获取新的附件名称
        String uuidFileName = FileUtils.generateUUIDFileName(file.getOriginalFilename());
        // 通过指定路径拼接附件全路径
        String fullFilePath = Paths.get(attachmentConfig.getUploadFilePath(), businessCode, uuidFileName).toString();
        fullFilePath = fullFilePath.replace("\\", "/");
        // 附件上传
        strategy.uploadFile(file, fullFilePath);
        return fullFilePath;
    }

    // 保存附件
    private String saveAttachment(SysAttachment sysAttachment) {
        sysAttachment
                .setStorageName(FileUtils.getFileNameByPath(sysAttachment.getPath()))
                .setExtensionName(FileUtils.getExtensionNameByFileName(sysAttachment.getStorageName()))
                .setStorageLocation(attachmentConfig.getUploadFileModel())
                .setClientType(LoginUserContext.getClientType());
        // 保存附件信息
        if (StringUtils.hasText(sysAttachment.getId())) {
            sysAttachmentMapper.updateById(sysAttachment);
        } else {
            sysAttachmentMapper.insert(sysAttachment);
        }
        return sysAttachment.getId();
    }

    // 分片上传中通过uploadId获取fullFilePath
    private String getChunksFullPathByUploadId(String uploadId) {
        // 通过uploadId获取fullFilePath
        String fullFilePath = redisCache.getCacheObject(RedisKeyPrefixEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX + uploadId, String.class);
        if (!StringUtils.hasText(fullFilePath)) {
            List<SysAttachment> list = lambdaQuery().select(SysAttachment::getPath).eq(SysAttachment::getUploadId, uploadId).list();
            if (list.isEmpty()) {
                throw new AttachmentException("获取分片路径失败");
            }
            fullFilePath = list.get(0).getPath();
            redisCache.setCacheObject(RedisKeyPrefixEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX + uploadId, fullFilePath);
        }

        return fullFilePath;
    }

    // 获取分片附件上传id，并缓存到redis
    private String chunksGetUploadId(String fullFilePath) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();
        String uploadId = attachmentStorageStrategy.getUploadId(fullFilePath);
        // uploadId和fullFilePath保存到redis
        redisCache.setCacheObject(RedisKeyPrefixEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX + uploadId, fullFilePath);
        return uploadId;
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
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(attachmentConfig.getUploadFileModel());
        if (attachmentStorageStrategy == null) {
            log.error("获取附件实现策略失败，请检查uploadFileModel策略配置，可选参数" + attachmentStorageStrategyMap.keySet());
            throw new ServiceException("获取附件实现策略失败");
        }
        return attachmentStorageStrategy;
    }
    // SysAttachment 参数填充
    private void fillParameters(MultipartFile file, SysAttachment sysAttachment) {
        if (!StringUtils.hasText(sysAttachment.getType())) {
            sysAttachment.setType(file.getContentType());
        }
        if (!StringUtils.hasText(sysAttachment.getSize())) {
            sysAttachment.setSize(String.valueOf(file.getSize()));
        }
        if (!StringUtils.hasText(sysAttachment.getOriginalName())) {
            sysAttachment.setOriginalName(file.getOriginalFilename());
        }
    }
}
