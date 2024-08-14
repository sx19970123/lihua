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

import java.util.List;

@Service("sysLoginLogService")
public class SysLoginLogServiceImpl implements SysLogService {

    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    @Override
    @Async
    public void insert(SysLogVO sysLogVO) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        BeanUtils.copyProperties(sysLogVO, sysLoginLog);
        sysLoginLogMapper.insert(sysLoginLog);
    }

    @Override
    public IPage<? extends SysLogVO> findPage(SysLogDTO sysLogDTO) {
        IPage<SysLoginLog> iPage = new Page<>(sysLogDTO.getPageNum(), sysLogDTO.getPageSize());

        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();

        // 操作人姓名
        if (StringUtils.hasText(sysLogDTO.getCreateName())) {
            queryWrapper.lambda().like(SysLogVO::getCreateName, sysLogDTO.getCreateName());
        }

        // 执行状态
        if (StringUtils.hasText(sysLogDTO.getExecuteStatus())) {
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

        sysLoginLogMapper.selectPage(iPage, queryWrapper);
        return iPage;
    }

    @Override
    public SysLogVO findById(String id) {
        return sysLoginLogMapper.selectById(id);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        sysLoginLogMapper.deleteBatchIds(ids);
    }

    @Override
    public void clearLog() {
        sysLoginLogMapper.delete(new QueryWrapper<>());
    }
}
