package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.system.entity.SysAttachment;
import com.lihua.system.mapper.SysAttachmentMapper;
import com.lihua.system.model.dto.SysAttachmentDTO;
import com.lihua.system.service.SysAttachmentService;
import com.lihua.system.strategy.AttachmentStorageStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment> implements SysAttachmentService {

    @Resource
    private Map<String, AttachmentStorageStrategy> attachmentStorageStrategyMap;

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
        return "";
    }

    @Override
    public List<String> getDownloadURL(List<String> pathList) {
        return List.of();
    }
}
