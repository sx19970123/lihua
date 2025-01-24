package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.system.entity.SysAttachment;
import com.lihua.system.mapper.SysAttachmentMapper;
import com.lihua.system.model.dto.SysAttachmentDTO;
import com.lihua.system.service.SysAttachmentService;
import com.lihua.system.strategy.AttachmentStorageStrategy;
import com.lihua.utils.crypt.AesUtils;
import com.lihua.utils.date.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment> implements SysAttachmentService {

    @Resource
    private Map<String, AttachmentStorageStrategy> attachmentStorageStrategyMap;

    @Value("${lihua.uploadFileModel}")
    private String uploadFileModel;

    @Value("${lihua.fileDownloadExpireTime}")
    private String fileDownloadExpireTime;


    @Override
    public IPage<SysAttachment> queryPage(SysAttachmentDTO sysAttachmentDTO) {
        return null;
    }

    @Override
    public SysAttachment queryById(String id) {
        return null;
    }

    @Override
    public String save(MultipartFile file) {
        return "";
    }

    @Override
    public void deleteByIds(List<SysAttachment> sysAttachmentList) {

    }

    @Override
    public void deleteByPath(String path) {

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
        String params;
        try {
            // 解密数据
            params = AesUtils.decryptToString(key, SysBaseEnum.ATTACHMENT_URL_KEY.getValue());
        } catch (Exception e) {
            log.error( e.getMessage(), e);
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

        return new File(splitParams[0]);
    }

    // 获取 AttachmentStorageStrategy 对应实现
    private AttachmentStorageStrategy getStrategy() {
        return attachmentStorageStrategyMap.get(uploadFileModel);
    }
}
