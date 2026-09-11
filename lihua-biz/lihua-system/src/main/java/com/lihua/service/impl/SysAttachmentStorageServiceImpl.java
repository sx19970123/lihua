package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lihua.attachment.config.AttachmentProperties;
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
import com.lihua.model.dto.AttachmentChunkMergeDTO;
import com.lihua.model.dto.AttachmentChunkStartDTO;
import com.lihua.model.dto.AttachmentFastUploadDTO;
import com.lihua.model.dto.AttachmentUploadDTO;
import com.lihua.model.vo.AttachmentUploadVO;
import com.lihua.model.vo.FastUploadResultVO;
import com.lihua.model.vo.SysAttachmentChunkVO;
import com.lihua.cache.manager.RedisCacheManager;
import com.lihua.cache.enums.RedisKeyPrefixEnum;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.service.SysAttachmentStorageService;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

@Service
public class SysAttachmentStorageServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment> implements SysAttachmentStorageService {

    // 不同附件存储方法实现策略
    @Resource
    private Map<String, AttachmentStorageStrategy> attachmentStorageStrategyMap;

    @Resource
    private RedisCacheManager redisCacheManager;

    @Resource
    private AttachmentProperties attachmentProperties;

    @Resource
    private SysAttachmentMapper sysAttachmentMapper;

    @Override
    public boolean existsAttachmentByMd5(String md5) {
        return findUploadableByMd5(md5) != null;
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
    public AttachmentUploadVO uploadAttachment(AttachmentUploadDTO uploadDTO) {
        MultipartFile file = uploadDTO.getFile();
        boolean isPublic = Boolean.TRUE.equals(uploadDTO.getPublic());
        checkUploadExtension(file.getOriginalFilename());
        SysAttachment attachment = new SysAttachment()
                .setOriginalName(file.getOriginalFilename())
                .setType(file.getContentType())
                .setSize(String.valueOf(file.getSize()))
                .setUploadMode("0")
                .setBusinessCode(uploadDTO.getBusinessCode())
                .setBusinessName(StringUtils.hasText(uploadDTO.getBusinessName()) ? uploadDTO.getBusinessName() : uploadDTO.getBusinessCode());
        try {
            attachment.setMd5(computeMd5(file));
            String path = upload(file, attachment.getBusinessCode());
            attachment.setPath(path).setStatus("0");
            saveAttachment(attachment);
            return buildUploadVO(attachment, isPublic);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            attachment.setStatus("1").setErrorMsg(e.getMessage());
            saveAttachment(attachment);
            throw new AttachmentException("附件上传失败");
        }
    }

    @Override
    public FastUploadResultVO fastUpload(AttachmentFastUploadDTO fastUploadDTO) {
        SysAttachment hitAttachment = findUploadableByMd5(fastUploadDTO.getMd5());
        // 未命中：客户端转普通上传
        if (hitAttachment == null) {
            return new FastUploadResultVO().setUploaded(false);
        }
        boolean isPublic = Boolean.TRUE.equals(fastUploadDTO.getPublic());
        // 复制命中行存储信息建新行（秒传=同 md5 已有物理文件，size/type 等由命中行回填，客户端不声明）
        SysAttachment attachment = new SysAttachment()
                .setOriginalName(fastUploadDTO.getOriginalName())
                .setMd5(fastUploadDTO.getMd5())
                .setUploadMode("2")
                .setPath(hitAttachment.getPath())
                .setType(hitAttachment.getType())
                .setSize(hitAttachment.getSize())
                .setBusinessCode(fastUploadDTO.getBusinessCode())
                .setBusinessName(StringUtils.hasText(fastUploadDTO.getBusinessName()) ? fastUploadDTO.getBusinessName() : fastUploadDTO.getBusinessCode())
                .setStatus("0");
        saveAttachment(attachment);
        FastUploadResultVO resultVO = new FastUploadResultVO();
        resultVO.setId(attachment.getId())
                .setPath(attachment.getPath())
                .setIsPublic(isPublic)
                .setUrl(buildUploadUrl(attachment, isPublic))
                .setOriginalName(attachment.getOriginalName())
                .setType(attachment.getType());
        resultVO.setUploaded(true);
        return resultVO;
    }

    @Override
    public SysAttachmentChunkVO chunksUploadAttachmentStart(AttachmentChunkStartDTO chunkStartDTO) {
        checkUploadExtension(chunkStartDTO.getOriginalName());
        SysAttachment attachment = new SysAttachment()
                .setOriginalName(chunkStartDTO.getOriginalName())
                .setMd5(chunkStartDTO.getMd5())
                .setSize(String.valueOf(chunkStartDTO.getSize()))
                .setUploadMode("1")
                .setBusinessCode(chunkStartDTO.getBusinessCode())
                .setBusinessName(StringUtils.hasText(chunkStartDTO.getBusinessName()) ? chunkStartDTO.getBusinessName() : chunkStartDTO.getBusinessCode())
                .setStatus("2");
        String path = buildUploadFilePath(attachment.getBusinessCode(), attachment.getOriginalName());
        attachment.setPath(path);
        try {
            // 获取附件id
            String uploadId = chunksGetUploadId(path);
            attachment.setUploadId(uploadId);
            // 保存附件信息
            String attachmentId = saveAttachment(attachment);
            return new SysAttachmentChunkVO(uploadId, attachmentId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            attachment.setStatus("1").setErrorMsg(e.getMessage());
            saveAttachment(attachment);
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
    public void chunksUpload(MultipartFile file, String uploadId, Integer index) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();
        attachmentStorageStrategy.chunksUploadFile(file, getChunksFullPathByUploadId(uploadId), index, uploadId);
    }

    @Override
    public AttachmentUploadVO chunksMerge(AttachmentChunkMergeDTO chunkMergeDTO, Integer total) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();
        String uploadId = chunkMergeDTO.getUploadId();
        // 按 uploadId 定位 chunk/start 建立的分片行（不信任客户端回传行 id）
        List<SysAttachment> attachments = lambdaQuery().eq(SysAttachment::getUploadId, uploadId).list();
        if (attachments.isEmpty()) {
            throw new AttachmentException("分片上传记录不存在");
        }
        SysAttachment attachment = attachments.get(0);
        // 幂等：行已是成功态说明本 uploadId 已完成合并，重复提交直接返回
        if ("0".equals(attachment.getStatus())) {
            return buildUploadVO(attachment, false);
        }
        try {
            String fullFilePath = getChunksFullPathByUploadId(uploadId);
            // 分片合并
            attachmentStorageStrategy.chunksMerge(fullFilePath, chunkMergeDTO.getMd5(), uploadId, total);
            return finishChunksMerge(attachment, chunkMergeDTO);
        } catch (Exception e) {
            // 并发重复提交时另一路可能已完成物理合并：目标文件在存则按成功收尾，不把成功行覆盖成失败
            if (attachmentStorageStrategy.isExists(getChunksFullPathByUploadId(uploadId))) {
                return finishChunksMerge(attachment, chunkMergeDTO);
            }
            log.error(e.getMessage(), e);
            attachment.setStatus("1").setErrorMsg(e.getMessage());
            saveAttachment(attachment);
            throw new AttachmentException("附件合并失败");
        } finally {
            // 删除redis缓存
            redisCacheManager.delete(RedisKeyPrefixEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX.getValue() + uploadId);
        }
    }

    // 合并成功收尾（正常完成与并发复查命中共用）；公开性以 chunk/start 声明为准，行级 is_public 列落地前暂按私密回显
    private AttachmentUploadVO finishChunksMerge(SysAttachment attachment, AttachmentChunkMergeDTO chunkMergeDTO) {
        attachment.setOriginalName(chunkMergeDTO.getOriginalName())
                .setMd5(chunkMergeDTO.getMd5())
                .setStatus("0");
        saveAttachment(attachment);
        return buildUploadVO(attachment, false);
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
        return strategy.getDownloadURL(path, originalName, expireTime != null && expireTime != 0 ? expireTime : attachmentProperties.getFileDownloadExpireTime());
    }


    @Override
    public ResponseEntity<StreamingResponseBody> localDownload(String key, String originName) {
        if (!"LOCAL".equals(attachmentProperties.getUploadFileModel())) {
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
        if (FileUtils.checkPath(filePath, attachmentProperties.getUploadFilePath())) {
            return AttachmentResponse.success(new File(filePath), originName);
        }

        throw new AttachmentException("下载失败，路径不匹配");
    }

    @Override
    public ResponseEntity<StreamingResponseBody> download(String fullPath) {
        List<String> uploadPublicBusinessCode = attachmentProperties.getUploadPublicBusinessCode();
        String uploadFilePath = attachmentProperties.getUploadFilePath();

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
        String fullFilePath = buildUploadFilePath(businessCode, file.getOriginalFilename());
        // 附件上传
        strategy.uploadFile(file, fullFilePath);
        return fullFilePath;
    }

    // 构造附件落盘路径并做写侧校验：目标必须位于附件根目录内（防路径穿越），分隔符统一为 "/"
    private String buildUploadFilePath(String businessCode, String originalName) {
        String uuidFileName = FileUtils.generateUUIDFileName(originalName);
        String fullFilePath = Paths.get(attachmentProperties.getUploadFilePath(), businessCode, uuidFileName).toString();
        if (!FileUtils.checkWritePath(fullFilePath, attachmentProperties.getUploadFilePath())) {
            throw new AttachmentException("非法的附件保存路径");
        }
        return fullFilePath.replace("\\", "/");
    }

    // 上传类型限制：可选配置 attachment.uploadAllowExtensions（空=不限制）；与附件公开性无关，由使用方按部署场景决定
    private void checkUploadExtension(String fileName) {
        List<String> allowExtensions = attachmentProperties.getUploadAllowExtensions();
        if (allowExtensions == null || allowExtensions.isEmpty()) {
            return;
        }
        String extensionName = FileUtils.getExtensionNameByFileName(fileName);
        String extension = extensionName == null ? "" : extensionName.substring(1).toLowerCase();
        boolean allowed = allowExtensions.stream().anyMatch(item -> {
            String normalized = item.startsWith(".") ? item.substring(1) : item;
            return normalized.equalsIgnoreCase(extension);
        });
        if (!allowed) {
            throw new ServiceException("不允许上传该类型附件，允许类型：" + String.join("/", allowExtensions));
        }
    }

    // 服务端计算文件流 md5（秒传判存键以服务端计算为准，防客户端伪造）
    private String computeMd5(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return DigestUtils.md5Hex(inputStream);
        } catch (IOException e) {
            throw new AttachmentException("附件md5计算失败");
        }
    }

    // 组装上传统一响应（url=首次链接：公开=永久链，私密=时效签名链）
    private AttachmentUploadVO buildUploadVO(SysAttachment attachment, boolean isPublic) {
        return new AttachmentUploadVO()
                .setId(attachment.getId())
                .setPath(attachment.getPath())
                .setIsPublic(isPublic)
                .setUrl(buildUploadUrl(attachment, isPublic))
                .setOriginalName(attachment.getOriginalName())
                .setType(attachment.getType());
    }

    // 组装首次访问链接（公开=永久链，私密=时效签名链）
    private String buildUploadUrl(SysAttachment attachment, boolean isPublic) {
        return isPublic
                ? "/system/attachment/storage/download/p?fullPath=" + URLEncoder.encode(attachment.getPath(), StandardCharsets.UTF_8)
                : getAttachmentURL(attachment.getPath(), attachment.getOriginalName(), null);
    }

    // 保存附件
    private String saveAttachment(SysAttachment sysAttachment) {
        sysAttachment
                .setStorageName(FileUtils.getFileNameByPath(sysAttachment.getPath()))
                .setExtensionName(FileUtils.getExtensionNameByFileName(sysAttachment.getStorageName()))
                .setStorageLocation(attachmentProperties.getUploadFileModel())
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
        String fullFilePath = redisCacheManager.getCacheObject(RedisKeyPrefixEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX.getValue() + uploadId, String.class);
        if (!StringUtils.hasText(fullFilePath)) {
            List<SysAttachment> list = lambdaQuery().select(SysAttachment::getPath).eq(SysAttachment::getUploadId, uploadId).list();
            if (list.isEmpty()) {
                throw new AttachmentException("获取分片路径失败");
            }
            fullFilePath = list.get(0).getPath();
            redisCacheManager.setCacheObject(RedisKeyPrefixEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX.getValue() + uploadId, fullFilePath, Duration.ofDays(30L));
        }

        return fullFilePath;
    }

    // 获取分片附件上传id，并缓存到redis
    private String chunksGetUploadId(String fullFilePath) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();
        String uploadId = attachmentStorageStrategy.getUploadId(fullFilePath);
        // uploadId和fullFilePath保存到redis
        redisCacheManager.setCacheObject(RedisKeyPrefixEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX.getValue() + uploadId, fullFilePath, Duration.ofDays(30L));
        return uploadId;
    }

    // 按 md5 查找物理文件仍存在的附件行（判存与秒传共用命中语义；同 md5 多行为常态——秒传复制行、重复上传各持文件，任一物理文件在即命中）
    private SysAttachment findUploadableByMd5(String md5) {
        AttachmentStorageStrategy strategy = getStrategy();
        List<SysAttachment> attachments = lambdaQuery()
                .eq(SysAttachment::getMd5, md5)
                .eq(SysAttachment::getDelFlag, "0")
                .eq(SysAttachment::getStatus, "0")
                .list();
        Set<String> checkedPaths = new HashSet<>();
        for (SysAttachment attachment : attachments) {
            String path = attachment.getPath();
            if (!StringUtils.hasText(path) || !checkedPaths.add(path)) {
                continue;
            }
            if (strategy.isExists(path)) {
                return attachment;
            }
        }
        return null;
    }

    // 获取 AttachmentStorageStrategy 对应实现
    private AttachmentStorageStrategy getStrategy() {
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(attachmentProperties.getUploadFileModel());
        if (attachmentStorageStrategy == null) {
            log.error("获取附件实现策略失败，请检查uploadFileModel策略配置，可选参数" + attachmentStorageStrategyMap.keySet());
            throw new ServiceException("获取附件实现策略失败");
        }
        return attachmentStorageStrategy;
    }
}
