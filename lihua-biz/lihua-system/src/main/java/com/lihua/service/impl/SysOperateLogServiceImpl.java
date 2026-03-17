package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.common.model.bridge.log.LogModel;
import com.lihua.entity.SysOperateLog;
import com.lihua.mapper.SysOperateLogMapper;
import com.lihua.model.dto.SysLogDTO;
import com.lihua.model.vo.SysLogVO;
import com.lihua.service.SysLogService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service("sysOperateLogService")
public class SysOperateLogServiceImpl implements SysLogService {

    @Resource
    private SysOperateLogMapper sysOperateLogMapper;

    @Override
    @EventListener(condition = "#logModel.typeCode != 'LOGIN'")
    public void insert(LogModel logModel) {
        SysOperateLog sysOperateLog = new SysOperateLog();
        BeanUtils.copyProperties(logModel, sysOperateLog);
        sysOperateLogMapper.insert(sysOperateLog);
    }

    @Override
    public IPage<? extends SysLogVO> queryPage(SysLogDTO sysLogDTO) {
        IPage<SysOperateLog> iPage = new Page<>(sysLogDTO.getPageNum(), sysLogDTO.getPageSize());

        QueryWrapper<SysOperateLog> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().select(SysOperateLog::getId,
                SysOperateLog::getDescription,
                SysOperateLog::getTypeMsg,
                SysOperateLog::getCreateName,
                SysOperateLog::getIpAddress,
                SysOperateLog::getRegion,
                SysOperateLog::getExecuteStatus,
                SysOperateLog::getCreateTime,
                SysOperateLog::getExecuteTime,
                SysOperateLog::getClientType);

        // 类型
        if (StringUtils.hasText(sysLogDTO.getTypeCode())) {
            queryWrapper.lambda().eq(SysLogVO::getTypeCode, sysLogDTO.getTypeCode());
        }

        // 操作人姓名
        if (StringUtils.hasText(sysLogDTO.getCreateName())) {
            queryWrapper.lambda().like(SysLogVO::getCreateName, sysLogDTO.getCreateName());
        }

        // 执行状态
        if (StringUtils.hasText(sysLogDTO.getExecuteStatus())) {
            queryWrapper.lambda().eq(SysLogVO::getExecuteStatus, sysLogDTO.getExecuteStatus());
        }

        // 客户端类型
        if (StringUtils.hasText(sysLogDTO.getClientType())) {
            queryWrapper.lambda().eq(SysLogVO::getClientType, sysLogDTO.getClientType());
        }

        // 描述
        if (StringUtils.hasText(sysLogDTO.getDescription())) {
            queryWrapper.lambda().like(SysLogVO::getDescription, sysLogDTO.getDescription());
        }

        // 执行时间范围
        List<LocalDate> createTimeList = sysLogDTO.getCreateTimeList();
        if (createTimeList != null && createTimeList.size() == 2) {
            queryWrapper.lambda().between(SysLogVO::getCreateTime,createTimeList.get(0), createTimeList.get(1).plusDays(1L));
        }

        queryWrapper.lambda().orderByDesc(SysLogVO::getId);

        sysOperateLogMapper.selectPage(iPage, queryWrapper);
        return iPage;
    }

    @Override
    public SysLogVO queryById(String id) {
        return sysOperateLogMapper.selectById(id);
    }

    @Override
    public SysLogVO queryByCacheKey(String cacheKey) {
        return null;
    }

    @Override
    public List<? extends SysLogVO> exportExcel(SysLogDTO sysLogDTO) {
        QueryWrapper<SysOperateLog> queryWrapper = new QueryWrapper<>();

        // 类型
        if (StringUtils.hasText(sysLogDTO.getTypeCode())) {
            queryWrapper.lambda().eq(SysLogVO::getTypeCode, sysLogDTO.getTypeCode());
        }

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

        queryWrapper.lambda().orderByDesc(SysLogVO::getId);
        return sysOperateLogMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        sysOperateLogMapper.deleteByIds(ids);
    }

    @Override
    public void clearLog() {
        sysOperateLogMapper.clear();
    }
}
