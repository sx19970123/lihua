package com.lihua.service.system.attachment.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.entity.system.SysAttachment;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.exception.ServiceException;
import com.lihua.mapper.system.SysAttachmentMapper;
import com.lihua.model.system.vo.SysAttachmentChunkVO;
import com.lihua.model.system.vo.SysAttachmentUrlVO;
import com.lihua.model.web.response.ApiResponse;
import com.lihua.service.system.attachment.SysAttachmentStorageService;
import com.lihua.strategy.system.attachment.AttachmentStorageStrategy;
import com.lihua.utils.crypt.AesUtils;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.file.FileUtils;
import com.lihua.utils.file.UrlFileUtils;
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
    private LihuaConfig lihuaConfig;

    // 可以通过url下载的附件后缀
    private static final List<String> UPLOADED_URL_SUFFIX = List.of("jpg", "jpeg", "png", "gif");

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
        sysAttachmentList.forEach(sysAttachment -> sysAttachment.setPath(getAttachmentURL(sysAttachment.getPath(), sysAttachment.getOriginalName(), null)));

        // 未查询出结果的数据集创建对象
        ids.forEach(id -> {
            SysAttachment attachment = new SysAttachment();
            attachment.setId(id).setOriginalName("附件丢失（附件id：" + id + "）").setStatus("error");
            sysAttachmentList.add(attachment);
        });

        return sysAttachmentList;
    }

    @Override
    public String uploadAttachment(MultipartFile file, SysAttachment sysAttachment) {
        try {
            String path = upload(file);
            sysAttachment.setPath(path).setStatus("0");
            return saveAttachment(sysAttachment);
        } catch (Exception e) {
            sysAttachment.setStatus("1").setErrorMsg(e.getMessage());
            saveAttachment(sysAttachment);
            throw new FileException("附件上传失败");
        }
    }

    @Override
    public List<String> batchUploadAttachment(MultipartFile[] files, List<SysAttachment> attachmentList) {
        // 遍历上传附件
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            SysAttachment sysAttachment = attachmentList.get(i);

            try {
                // 执行上传，返回路径
                String path = upload(file);
                // 设置成功状态
                sysAttachment.setPath(path).setStatus("0");
            } catch (Exception e) {
                // 记录错误状态
                sysAttachment.setStatus("1").setErrorMsg(e.getMessage());
            }
        }

        return batchSaveAttachment(attachmentList);
    }

    @Override
    public SysAttachmentUrlVO urlUploadAttachment(SysAttachment sysAttachment) {
        // url 转为 MultipartFile 对象
        MultipartFile multipartFile = UrlFileUtils.urlToMultipartFile(sysAttachment.getUrl());
        try {
            // 判断url附件类型是否在UPLOADED_URL_SUFFIX定义的集合中
            boolean isEmpty = UPLOADED_URL_SUFFIX.stream()
                    .filter(suffix -> StringUtils.hasText(multipartFile.getOriginalFilename()) && multipartFile.getOriginalFilename().toLowerCase().endsWith(suffix))
                    .toList().isEmpty();
            if (isEmpty) {
                throw new FileException("附件类型不在UPLOADED_URL_SUFFIX定义中，不进行上传");
            }

            // 附件上传
            String path = upload(multipartFile);
            sysAttachment
                    .setPath(path)
                    .setStatus("0")
                    .setOriginalName(multipartFile.getOriginalFilename())
                    .setSize(String.valueOf(multipartFile.getSize()))
                    .setType(multipartFile.getContentType());
            // 保存附件返回id
            String id = saveAttachment(sysAttachment);
            // 返回原路径和id
            return new SysAttachmentUrlVO(id, sysAttachment.getUrl());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sysAttachment.setStatus("1").setErrorMsg(e.getMessage());
            saveAttachment(sysAttachment);
            // 异常返回原url
            throw new FileException(sysAttachment.getUrl());
        }
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
    public SysAttachmentChunkVO chunksUploadAttachmentStart(SysAttachment sysAttachment) {
        String path = lihuaConfig.getUploadFilePath() + FileUtils.generateUUIDFileName(sysAttachment.getOriginalName());
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
            throw new FileException(e.getMessage());
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
            throw new FileException("附件合并失败");
        } finally {
            // 删除redis缓存
            redisCache.delete(SysBaseEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX + uploadId);
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
        return strategy.getDownloadURL(path, originalName, expireTime != null && expireTime != 0 ? expireTime : lihuaConfig.getFileDownloadExpireTime());
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
        return ApiResponse.success(inputStream, StringUtils.hasText(fileName) ? fileName : sysAttachment.getOriginalName(), sysAttachment.getSize());
    }

    @Override
    public ResponseEntity<StreamingResponseBody> localDownload(String key, String originName) {
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
        return ApiResponse.success(new File(splitParams[0]), originName);
    }

    @Override
    public ResponseEntity<StreamingResponseBody> exportDownload(String path, String fileName) {
        if (StringUtils.hasText(path) && (path.contains(lihuaConfig.getExportFilePath()) || path.replace("\\", "/").contains(lihuaConfig.getExportFilePath()))) {
            return ApiResponse.success(new File(path), fileName, true);
        }
        throw new FileException("下载失败，路径不匹配");
    }

    // 附件上传方法
    private String upload(MultipartFile file) {
        AttachmentStorageStrategy strategy = getStrategy();
        // 获取新的附件名称
        String uuidFileName = FileUtils.generateUUIDFileName(file.getOriginalFilename());
        // 通过指定路径拼接附件全路径
        String fullFilePath = lihuaConfig.getUploadFilePath() + uuidFileName;
        // 附件上传
        strategy.uploadFile(file, fullFilePath);
        return fullFilePath;
    }

    // 保存附件
    private String saveAttachment(SysAttachment sysAttachment) {
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

    // 批量保存附件
    @Transactional
    private List<String> batchSaveAttachment(List<SysAttachment> sysAttachmentList) {
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

    // 分片上传中通过uploadId获取fullFilePath
    private String getChunksFullPathByUploadId(String uploadId) {
        // 通过uploadId获取fullFilePath
        String fullFilePath = redisCache.getCacheObject(SysBaseEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX + uploadId, String.class);
        if (!StringUtils.hasText(fullFilePath)) {
            List<SysAttachment> list = lambdaQuery().select(SysAttachment::getPath).eq(SysAttachment::getUploadId, uploadId).list();
            if (list.isEmpty()) {
                throw new FileException("获取分片路径失败");
            }
            fullFilePath = list.get(0).getPath();
            redisCache.setCacheObject(SysBaseEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX + uploadId, fullFilePath);
        }

        return fullFilePath;
    }

    // 获取分片附件上传id，并缓存到redis
    private String chunksGetUploadId(String fullFilePath) {
        AttachmentStorageStrategy attachmentStorageStrategy = getStrategy();
        String uploadId = attachmentStorageStrategy.getUploadId(fullFilePath);
        // uploadId和fullFilePath保存到redis
        redisCache.setCacheObject(SysBaseEnum.CHUNK_UPLOAD_ID_REDIS_PREFIX + uploadId, fullFilePath);
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
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(lihuaConfig.getUploadFileModel());
        if (attachmentStorageStrategy == null) {
            log.error("获取附件实现策略失败，请检查uploadFileModel策略配置，可选参数" + attachmentStorageStrategyMap.keySet());
            throw new ServiceException("获取附件实现策略失败");
        }
        return attachmentStorageStrategy;
    }
}
