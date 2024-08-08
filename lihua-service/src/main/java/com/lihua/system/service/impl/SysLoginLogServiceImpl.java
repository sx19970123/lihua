package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.system.entity.SysLoginLog;
import com.lihua.system.mapper.SysLoginLogMapper;
import com.lihua.system.model.dto.SysLogDTO;
import com.lihua.system.model.vo.SysLogVO;
import com.lihua.system.service.SysLogService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("sysLoginLogService")
public class SysLoginLogServiceImpl implements SysLogService {

    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    @Override
    @Async
    public String insert(SysLogVO sysLogVO) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        BeanUtils.copyProperties(sysLogVO, sysLoginLog);
        sysLoginLogMapper.insert(sysLoginLog);
        return sysLoginLog.getId();
    }

    @Override
    public IPage<? extends SysLogVO> findPage(SysLogDTO sysLogDTO) {
        IPage<SysLoginLog> iPage = new Page<>(sysLogDTO.getPageNum(), sysLogDTO.getPageSize());

        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();

        // 类型
        if (StringUtils.hasText(sysLogDTO.getType())) {
            queryWrapper.lambda().eq(SysLogVO::getType, sysLogDTO.getType());
        }

        // 描述
        if (StringUtils.hasText(sysLogDTO.getDescription())) {
            queryWrapper.lambda().like(SysLogVO::getDescription, sysLogDTO.getDescription());
        }

        // 执行时间范围
        if (sysLogDTO.getStartTime() != null && sysLogDTO.getEndTime() != null) {
            queryWrapper.lambda().between(SysLogVO::getCreateTime, sysLogDTO.getStartTime(), sysLogDTO.getEndTime());
        }

        sysLoginLogMapper.selectPage(iPage, queryWrapper);
        return iPage;
    }

    @Override
    public SysLogVO findById(String id) {
        return sysLoginLogMapper.selectById(id);
    }
}
