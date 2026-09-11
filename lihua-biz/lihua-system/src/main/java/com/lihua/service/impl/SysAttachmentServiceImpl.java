package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lihua.attachment.enums.AttachmentStatusEnum;
import com.lihua.common.exception.ServiceException;
import com.lihua.entity.SysAttachment;
import com.lihua.mapper.SysAttachmentMapper;
import com.lihua.model.dto.SysAttachmentDTO;
import com.lihua.model.vo.SysAttachmentVO;
import com.lihua.service.SysAttachmentService;
import com.lihua.service.SysAttachmentStorageService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
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
    public IPage<SysAttachmentVO> queryPage(SysAttachmentDTO sysAttachmentDTO) {
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
        //  客户端类型
        if (StringUtils.hasText(sysAttachmentDTO.getClientType())) {
            queryWrapper.lambda().eq(SysAttachment::getClientType, sysAttachmentDTO.getClientType());
        }
        //  上传时间
        List<LocalDate> createTimeList = sysAttachmentDTO.getCreateTimeList();
        if (createTimeList != null && createTimeList.size() == 2) {
            queryWrapper.lambda().between(SysAttachment::getCreateTime, createTimeList.get(0), createTimeList.get(1).plusDays(1L));
        }
        queryWrapper.lambda().orderByDesc(SysAttachment::getCreateTime);
        sysAttachmentMapper.selectPage(iPage, queryWrapper);
        return iPage.convert(this::toVO);
    }

    // 实体转对外展示模型（字段白名单）
    private SysAttachmentVO toVO(SysAttachment attachment) {
        SysAttachmentVO vo = new SysAttachmentVO();
        BeanUtils.copyProperties(attachment, vo);
        return vo;
    }

    @Override
    public SysAttachmentVO queryById(String id) {
        return sysAttachmentMapper.queryById(id);
    }


    @Override
    @Transactional
    public void deleteByIds(List<String> ids) {
        Long count = lambdaQuery()
                .or(wrapper -> wrapper.eq(SysAttachment::getStatus, AttachmentStatusEnum.FAIL.getValue()).or().eq(SysAttachment::getStatus, AttachmentStatusEnum.BUSINESS_DELETED.getValue()))
                .in(SysAttachment::getId, ids)
                .count();

        if (count != ids.size()) {
            throw new ServiceException("上传成功、分片上传中状态附件不允许删除");
        }

        // 调用强制删除（同类自调用：外层事务已覆盖全部操作，无需代理调用）
        forceDelete(ids);
    }

    @Override
    @Transactional
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
                .eq(SysAttachment::getStatus, AttachmentStatusEnum.SUCCESS.getValue())
                .isNotNull(SysAttachment::getPath)
                .one();

        if (sysAttachment == null) {
            return null;
        }

        return sysAttachmentStorageService.getAttachmentURL(sysAttachment.getPath(), sysAttachment.getOriginalName(), expireTime);
    }
}
