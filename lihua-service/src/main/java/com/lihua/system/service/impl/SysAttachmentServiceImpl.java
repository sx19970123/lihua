package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.exception.ServiceException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
    private final List<String> publicBusinessCodeList = List.of("");

    @Override
    public IPage<SysAttachment> queryPage(SysAttachmentDTO sysAttachmentDTO) {
        return null;
    }

    @Override
    public SysAttachment queryById(String id) {
        return null;
    }

    @Override
    public boolean existsAttachmentByMd5(String md5) {
        AttachmentStorageStrategy strategy = getStrategy();
        // 根据md5查询数据库是否存在
        SysAttachment attachment = queryOneByMd5(md5);
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
        SysAttachment attachment = queryOneByMd5(sysAttachment.getMd5());
        if (attachment == null) {
            return null;
        }
        // 获取路径
        String path = attachment.getPath();
        sysAttachment
                .setPath(path)
                .setUploadStatus("0");
        // 插入新数据
        saveAttachment(sysAttachment);
        return path;
    }

    @Override
    public List<SysAttachment> queryAttachmentInfoByPathList(List<String> pathList) {
        List<SysAttachment> sysAttachmentList = lambdaQuery()
                .select(SysAttachment::getId, SysAttachment::getPath, SysAttachment::getOriginalName)
                .in(SysAttachment::getPath, pathList)
                .list();
        // 获取文件访问路径
        sysAttachmentList.forEach(sysAttachment -> sysAttachment.setPath(getDownloadURL(sysAttachment.getPath())));
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
    public String upload(MultipartFile file) {
        AttachmentStorageStrategy strategy = getStrategy();
        return strategy.uploadFile(file);
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
        SysAttachment sysAttachment = queryOneByPath(path);
        if (sysAttachment != null) {
            return sysAttachment.getOriginalName();
        }
        return null;
    }

    @Override
    public void deleteByIds(List<SysAttachment> sysAttachmentList) {

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
    public String getDownloadURL(String path) {
        AttachmentStorageStrategy strategy = getStrategy();
        return strategy.getDownloadURL(path, Long.parseLong(fileDownloadExpireTime));
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
    public File publicDownload(String path) {
        // 根据路径和公开业务编码进行查询
        SysAttachment sysAttachment = queryOne(path, null, publicBusinessCodeList);

        if (sysAttachment == null) {
            return null;
        }

        AttachmentStorageStrategy strategy = getStrategy();
        return strategy.download(path, sysAttachment.getOriginalName());

    }

    // 根据 path 查询单条数据
    private SysAttachment queryOneByPath(String path) {
        return queryOne(path, null, null);
    }

    // 根据 md5 查询单条数据
    private SysAttachment queryOneByMd5(String md5) {
        return queryOne(null, md5, null);
    }

    // 根据需要条件查询单条数据
    private SysAttachment queryOne(String path, String md5, List<String> publicBusinessCodeList) {

        LambdaQueryChainWrapper<SysAttachment> chainWrapper = lambdaQuery();

        if (StringUtils.hasText(path)) {
            chainWrapper.eq(SysAttachment::getPath, path);
        }

        if (StringUtils.hasText(md5)) {
            chainWrapper.eq(SysAttachment::getMd5, md5);
        }

        if (publicBusinessCodeList != null && !publicBusinessCodeList.isEmpty()) {
            chainWrapper.in(SysAttachment::getBusinessCode, publicBusinessCodeList);
        }

        List<SysAttachment> list = chainWrapper
                .eq(SysAttachment::getDelFlag, "0")
                .eq(SysAttachment::getUploadStatus, "0")
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
