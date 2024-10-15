package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.system.entity.SysLoginLog;
import com.lihua.system.mapper.SysLoginLogMapper;
import com.lihua.system.model.dto.SysLogDTO;
import com.lihua.system.model.vo.SysLogVO;
import com.lihua.system.service.SysLogService;
import com.lihua.utils.excel.ExcelUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("sysLoginLogService")
public class SysLoginLogServiceImpl implements SysLogService {

    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    @Override
    public void insert(SysLogVO sysLogVO) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        BeanUtils.copyProperties(sysLogVO, sysLoginLog);
        sysLoginLogMapper.insert(sysLoginLog);
    }

    @Override
    public IPage<? extends SysLogVO> findPage(SysLogDTO sysLogDTO) {
        IPage<SysLoginLog> iPage = new Page<>(sysLogDTO.getPageNum(), sysLogDTO.getPageSize());

        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().select(SysLoginLog::getId,
                SysLoginLog::getDescription,
                SysLoginLog::getTypeMsg,
                SysLoginLog::getCreateName,
                SysLoginLog::getIpAddress,
                SysLoginLog::getExecuteStatus,
                SysLoginLog::getCreateTime,
                SysLoginLog::getExecuteTime,
                SysLoginLog::getUsername,
                SysLoginLog::getErrorMsg);

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

        // 登录时间
        if (sysLogDTO.getCreateTimeList() != null && !sysLogDTO.getCreateTimeList().isEmpty()) {
            queryWrapper.lambda().between(SysLogVO::getCreateTime, sysLogDTO.getCreateTimeList().get(0), sysLogDTO.getCreateTimeList().get(1));
        }

        queryWrapper.lambda().orderByDesc(SysLogVO::getId);

        sysLoginLogMapper.selectPage(iPage, queryWrapper);
        return iPage;
    }

    @Override
    public SysLogVO findById(String id) {
        return sysLoginLogMapper.selectById(id);
    }

    @Override
    public SysLogVO findByCacheKey(String cacheKey) {
        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysLoginLog::getCacheKey, cacheKey);
        return sysLoginLogMapper.selectOne(queryWrapper);
    }

    @Override
    public String exportExcel(SysLogDTO sysLogDTO) {

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

        queryWrapper.lambda().eq(SysLogVO::getDelFlag, "0").orderByDesc(SysLogVO::getId);

        List<SysLoginLog> sysLoginLogs = sysLoginLogMapper.selectList(queryWrapper);

        return ExcelUtils.excelExport(sysLoginLogs, SysLoginLog.class, "登录日志");
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
