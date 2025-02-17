package com.lihua.service.system.attachment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.exception.ServiceException;
import com.lihua.entity.system.SysAttachment;
import com.lihua.mapper.system.SysAttachmentMapper;
import com.lihua.model.system.dto.SysAttachmentDTO;
import com.lihua.model.system.vo.SysAttachmentVO;
import com.lihua.service.system.attachment.SysAttachmentStorageService;
import com.lihua.service.system.attachment.SysAttachmentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.util.List;

@Service
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment> implements SysAttachmentService {

    @Resource
    private SysAttachmentMapper sysAttachmentMapper;

    @Resource
    private SysAttachmentStorageService sysAttachmentStorageService;

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
        //  上传方式
        if (StringUtils.hasText(sysAttachmentDTO.getUploadMode())) {
            queryWrapper.lambda().eq(SysAttachment::getUploadMode, sysAttachmentDTO.getUploadMode());
        }
        //  业务名称
        if (StringUtils.hasText(sysAttachmentDTO.getBusinessName())) {
            queryWrapper.lambda().like(SysAttachment::getBusinessName, sysAttachmentDTO.getBusinessName());
        }
        //  上传时间
        List<LocalDate> createTimeList = sysAttachmentDTO.getCreateTimeList();
        if (createTimeList != null && createTimeList.size() == 2) {
            queryWrapper.lambda().between(SysAttachment::getCreateTime, createTimeList.get(0), createTimeList.get(1).plusDays(1L));
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
    @Transactional
    public void deleteByIds(List<String> ids) {
        Long count = lambdaQuery()
                .or(wrapper -> wrapper.eq(SysAttachment::getStatus, "1").or().eq(SysAttachment::getStatus, "3"))
                .in(SysAttachment::getId, ids)
                .count();

        if (count != ids.size()) {
            throw new ServiceException("上传成功、分片上传中状态附件不允许删除");
        }

        // 调用强制删除
        forceDelete(ids);
    }

    @Override
    public void forceDelete(List<String> ids) {
        // 根据ids获取可删除附件的路径
        List<String> deletablePathList = sysAttachmentMapper.queryDeletablePathByIds(ids);
        // 删除数据库记录
        removeByIds(ids);

        if (!deletablePathList.isEmpty()) {
            // 删除服务器附件
            sysAttachmentStorageService.deleteFiles(deletablePathList);
        }
    }

    @Override
    public String getDownloadURL(String id, Integer expireTime) {
        SysAttachment sysAttachment = lambdaQuery()
                .select(SysAttachment::getPath, SysAttachment::getOriginalName)
                .eq(SysAttachment::getId, id)
                .eq(SysAttachment::getStatus, "0")
                .isNotNull(SysAttachment::getPath)
                .one();

        if (sysAttachment == null) {
            return null;
        }

        return sysAttachmentStorageService.getAttachmentURL(sysAttachment.getPath(), sysAttachment.getOriginalName(), expireTime);
    }
}
