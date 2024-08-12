package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.system.entity.SysOperateLog;
import com.lihua.system.mapper.SysOperateLogMapper;
import com.lihua.system.model.dto.SysLogDTO;
import com.lihua.system.model.vo.SysLogVO;
import com.lihua.system.service.SysLogService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("sysOperateLogService")
public class SysOperateLogServiceImpl implements SysLogService {

    @Resource
    private SysOperateLogMapper sysOperateLogMapper;

    @Override
    @Async
    public void insert(SysLogVO sysLogVO) {
        SysOperateLog sysOperateLog = new SysOperateLog();
        BeanUtils.copyProperties(sysLogVO, sysOperateLog);
        sysOperateLogMapper.insert(sysOperateLog);
    }

    @Override
    public IPage<? extends SysLogVO> findPage(SysLogDTO sysLogDTO) {
        IPage<SysOperateLog> iPage = new Page<>(sysLogDTO.getPageNum(), sysLogDTO.getPageSize());

        QueryWrapper<SysOperateLog> queryWrapper = new QueryWrapper<>();

        // 类型
        if (StringUtils.hasText(sysLogDTO.getTypeCode())) {
            queryWrapper.lambda().eq(SysLogVO::getTypeCode, sysLogDTO.getTypeCode());
        }

        // 操作人姓名
        if (!StringUtils.hasText(sysLogDTO.getCreateName())) {
            queryWrapper.lambda().like(SysLogVO::getCreateName, sysLogDTO.getCreateName());
        }

        // 执行状态
        if (!StringUtils.hasText(sysLogDTO.getExecuteStatus())) {
            queryWrapper.lambda().eq(SysLogVO::getExecuteStatus, sysLogDTO.getExecuteStatus());
        }

        // 描述
        if (StringUtils.hasText(sysLogDTO.getDescription())) {
            queryWrapper.lambda().like(SysLogVO::getDescription, sysLogDTO.getDescription());
        }

        // 执行时间范围
        if (sysLogDTO.getCreateTimeList() != null && !sysLogDTO.getCreateTimeList().isEmpty()) {
            queryWrapper.lambda().between(SysLogVO::getCreateTime, sysLogDTO.getCreateTimeList().get(0), sysLogDTO.getCreateTimeList().get(1));
        }

        sysOperateLogMapper.selectPage(iPage, queryWrapper);
        return iPage;
    }

    @Override
    public SysLogVO findById(String id) {
        return sysOperateLogMapper.selectById(id);
    }
}
