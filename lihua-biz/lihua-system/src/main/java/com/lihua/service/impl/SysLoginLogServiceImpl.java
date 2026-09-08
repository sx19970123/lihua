package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.common.model.bridge.log.LogModel;
import com.lihua.entity.SysLoginLog;
import com.lihua.mapper.SysLoginLogMapper;
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

@Service("sysLoginLogService")
public class SysLoginLogServiceImpl implements SysLogService {

    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    @Override
    @EventListener(condition = "#logModel.typeCode == 'LOGIN'")
    public void insert(LogModel logModel) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        BeanUtils.copyProperties(logModel, sysLoginLog);
        sysLoginLogMapper.insert(sysLoginLog);
    }

    @Override
    public IPage<? extends SysLogVO> queryPage(SysLogDTO sysLogDTO) {
        IPage<SysLoginLog> iPage = new Page<>(sysLogDTO.getPageNum(), sysLogDTO.getPageSize());

        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().select(SysLoginLog::getId,
                SysLoginLog::getDescription,
                SysLoginLog::getTypeMsg,
                SysLoginLog::getCreateName,
                SysLoginLog::getIpAddress,
                SysLoginLog::getRegion,
                SysLoginLog::getExecuteStatus,
                SysLoginLog::getCreateTime,
                SysLoginLog::getExecuteTime,
                SysLoginLog::getUsername,
                SysLoginLog::getErrorMsg,
                SysLoginLog::getClientType);

        // 用户名
        if (StringUtils.hasText(sysLogDTO.getUsername())) {
            queryWrapper.lambda().like(SysLogVO::getUsername, sysLogDTO.getUsername());
        }

        // 操作人姓名
        if (StringUtils.hasText(sysLogDTO.getCreateName())) {
            queryWrapper.lambda().like(SysLogVO::getCreateName, sysLogDTO.getCreateName());
        }

        // 登录状态
        if (StringUtils.hasText(sysLogDTO.getExecuteStatus())) {
            queryWrapper.lambda().eq(SysLogVO::getExecuteStatus, sysLogDTO.getExecuteStatus());
        }

        // 客户端类型
        if (StringUtils.hasText(sysLogDTO.getClientType())) {
            queryWrapper.lambda().eq(SysLogVO::getClientType, sysLogDTO.getClientType());
        }

        // 登录时间
        List<LocalDate> createTimeList = sysLogDTO.getCreateTimeList();
        if (createTimeList != null && createTimeList.size() == 2) {
            queryWrapper.lambda().between(SysLogVO::getCreateTime,createTimeList.get(0), createTimeList.get(1).plusDays(1L));
        }

        queryWrapper.lambda().orderByDesc(SysLogVO::getId);

        sysLoginLogMapper.selectPage(iPage, queryWrapper);
        return iPage;
    }

    @Override
    public SysLogVO queryById(String id) {
        return sysLoginLogMapper.selectById(id);
    }

    @Override
    public SysLogVO queryByCacheKey(String cacheKey) {
        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysLoginLog::getCacheKey, cacheKey);
        return sysLoginLogMapper.selectOne(queryWrapper);
    }

    @Override
    public List<? extends SysLogVO> exportExcel(SysLogDTO sysLogDTO) {

        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();

        // 查询列对齐导出列，不载入 errorStack 等非导出大字段
        queryWrapper.lambda().select(SysLoginLog::getId,
                SysLoginLog::getDescription,
                SysLoginLog::getTypeMsg,
                SysLoginLog::getExecuteStatus,
                SysLoginLog::getClassName,
                SysLoginLog::getMethodName,
                SysLoginLog::getParams,
                SysLoginLog::getResult,
                SysLoginLog::getCreateName,
                SysLoginLog::getCreateTime,
                SysLoginLog::getExecuteTime,
                SysLoginLog::getUrl,
                SysLoginLog::getIpAddress,
                SysLoginLog::getRegion,
                SysLoginLog::getClientType);

        // 用户名
        if (StringUtils.hasText(sysLogDTO.getUsername())) {
            queryWrapper.lambda().like(SysLogVO::getUsername, sysLogDTO.getUsername());
        }

        // 操作人姓名
        if (StringUtils.hasText(sysLogDTO.getCreateName())) {
            queryWrapper.lambda().like(SysLogVO::getCreateName, sysLogDTO.getCreateName());
        }

        // 登录状态
        if (StringUtils.hasText(sysLogDTO.getExecuteStatus())) {
            queryWrapper.lambda().eq(SysLogVO::getExecuteStatus, sysLogDTO.getExecuteStatus());
        }

        // 客户端类型
        if (StringUtils.hasText(sysLogDTO.getClientType())) {
            queryWrapper.lambda().eq(SysLogVO::getClientType, sysLogDTO.getClientType());
        }

        // 登录时间
        List<LocalDate> createTimeList = sysLogDTO.getCreateTimeList();
        if (createTimeList != null && createTimeList.size() == 2) {
            queryWrapper.lambda().between(SysLogVO::getCreateTime,createTimeList.get(0), createTimeList.get(1).plusDays(1L));
        }

        queryWrapper.lambda().orderByDesc(SysLogVO::getId);
        return sysLoginLogMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        sysLoginLogMapper.deleteByIds(ids);
    }

    @Override
    public void clearLog() {
        sysLoginLogMapper.clear();
    }
}
