package com.lihua.service.system.log.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.entity.system.SysOperateLog;
import com.lihua.mapper.system.SysOperateLogMapper;
import com.lihua.model.system.dto.SysLogDTO;
import com.lihua.model.system.vo.SysLogVO;
import com.lihua.service.system.log.SysLogService;
import com.lihua.utils.excel.ExcelUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service("sysOperateLogService")
public class SysOperateLogServiceImpl implements SysLogService {

    @Resource
    private SysOperateLogMapper sysOperateLogMapper;

    @Override
    public void insert(SysLogVO sysLogVO) {
        SysOperateLog sysOperateLog = new SysOperateLog();
        BeanUtils.copyProperties(sysLogVO, sysOperateLog);
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
                SysOperateLog::getExecuteStatus,
                SysOperateLog::getCreateTime,
                SysOperateLog::getExecuteTime);

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
    public String exportExcel(SysLogDTO sysLogDTO) {

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

        queryWrapper.lambda().eq(SysLogVO::getDelFlag, "0").orderByDesc(SysLogVO::getId);

        List<SysOperateLog> sysOperateLogs = sysOperateLogMapper.selectList(queryWrapper);

        return ExcelUtils.excelExport(sysOperateLogs, SysOperateLog.class, "操作日志");
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
