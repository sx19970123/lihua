package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    public String save(MultipartFile file, String businessCode, String businessName) {
        AttachmentStorageStrategy strategy = getStrategy();
        // 构建系统附件对象
        SysAttachment sysAttachment = new SysAttachment();

        // 文件上传至服务器
        try {
            String fullPath = strategy.uploadFile(file, businessCode);
            sysAttachment
                    .setPath(fullPath)
                    .setUploadStatus("0")
                    .setStorageName(FileUtils.getFileNameByPath(fullPath))
                    .setOriginalName(file.getOriginalFilename())
                    .setExtensionName(FileUtils.getExtensionNameByFileName(sysAttachment.getStorageName()))
                    .setType(file.getContentType());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sysAttachment
                    .setErrorMsg(e.getMessage())
                    .setUploadStatus("1");
        }

        // 完善附件数据
        sysAttachment
                .setSize(file.getSize())
                .setBusinessCode(businessCode)
                .setBusinessName(businessName)
                .setStorageLocation(uploadFileModel)
                .setUploadMode("0")
                .setCreateId(LoginUserContext.getUserId())
                .setCreateTime(DateUtils.now())
                .setDelFlag("0");
        // 保存附件信息
        save(sysAttachment);

        if ("0".equals(sysAttachment.getUploadStatus())) {
            return sysAttachment.getPath();
        } else {
            throw new FileException(sysAttachment.getErrorMsg());
        }
    }

    @Override
    public String chunksSave(SysAttachment sysAttachment) {
        // 根据md5值查询数据库
        SysAttachment dbData = lambdaQuery()
                .select(SysAttachment::getId)
                .eq(SysAttachment::getMd5, sysAttachment.getMd5())
                .one();
        // dbData不为空，为SysAttachment设置id，否则设置创建人/时间
        if (dbData != null) {
            sysAttachment.setId(dbData.getId());
        } else {
            sysAttachment.setCreateId(LoginUserContext.getUserId()).setCreateTime(DateUtils.now());
        }
        // 完善附件数据
        sysAttachment.setUploadMode("1")
                .setStorageLocation(uploadFileModel)
                .setDelFlag("0");
        // 调用mp的更新保存方法
        saveOrUpdate(sysAttachment);
        return sysAttachment.getId();
    }

    @Override
    public List<Integer> chunksUploadedIndex(String md5) {
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(uploadFileModel);
        List<Integer> tempChunksIndexList = attachmentStorageStrategy.getTempChunksIndex(md5);
        return tempChunksIndexList == null ? new ArrayList<>() : tempChunksIndexList;
    }

    @Override
    public void chunksUpload(MultipartFile file, String md5, String index) {
        AttachmentStorageStrategy attachmentStorageStrategy = attachmentStorageStrategyMap.get(uploadFileModel);
        attachmentStorageStrategy.uploadTempFile(file, Path.of(uploadFilePath,"temporary", md5, index).toString());
    }

    @Override
    public String queryOriginFileName(String path) {
        SysAttachment sysAttachment = lambdaQuery().select(SysAttachment::getOriginalName).eq(SysAttachment::getPath, path).one();
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
        SysAttachment sysAttachment = lambdaQuery()
                .eq(SysAttachment::getPath, path)
                .in(SysAttachment::getBusinessCode, publicBusinessCodeList)
                .one();

        if (sysAttachment == null) {
            return null;
        }

        AttachmentStorageStrategy strategy = getStrategy();
        return strategy.download(path, sysAttachment.getOriginalName());

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
