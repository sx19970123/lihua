package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.exception.ServiceException;
import com.lihua.system.entity.SysDictType;
import com.lihua.system.mapper.SysDictTypeMapper;
import com.lihua.system.model.dto.SysDictTypeDTO;
import com.lihua.system.service.SysDictDataService;
import com.lihua.system.service.SysDictTypeService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SysDictTypeServiceImpl implements SysDictTypeService {

    @Resource
    private SysDictTypeMapper sysDictTypeMapper;

    @Resource
    private SysDictDataService sysDictDataService;


    @Override
    public IPage<SysDictType> findPage(SysDictTypeDTO dictTypeDTO) {
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        IPage<SysDictType> page = new Page<>(dictTypeDTO.getPageNum(), dictTypeDTO.getPageSize());

        // 字典名称
        if (StringUtils.hasText(dictTypeDTO.getName())) {
            queryWrapper
                    .lambda()
                    .like(SysDictType::getName,dictTypeDTO.getName());
        }
        // 字典编码
        if (StringUtils.hasText(dictTypeDTO.getCode())) {
            queryWrapper
                    .lambda()
                    .like(SysDictType::getCode,dictTypeDTO.getCode());
        }
        // 字典状态
        if (StringUtils.hasText(dictTypeDTO.getStatus())) {
            queryWrapper
                    .lambda()
                    .eq(SysDictType::getStatus,dictTypeDTO.getStatus());
        }
        // 创建时间
        if (dictTypeDTO.getStartEndTime() != null && !dictTypeDTO.getStartEndTime().isEmpty()) {
            queryWrapper
                    .lambda()
                    .between(SysDictType::getCreateTime,dictTypeDTO.getStartEndTime().get(0),dictTypeDTO.getStartEndTime().get(1));
        }

        sysDictTypeMapper.selectPage(page,queryWrapper);
        return page;
    }

    @Override
    public SysDictType findById(String id) {
        return sysDictTypeMapper.selectById(id);
    }

    @Override
    @Transactional
    public String save(SysDictType sysDictType) {
        checkCode(sysDictType.getId(),sysDictType.getCode());
        if (!StringUtils.hasText(sysDictType.getId())) {
            return insert(sysDictType);
        }

        return updateById(sysDictType);
    }

    private String insert(SysDictType sysDictType) {
        sysDictType.setCreateId(LoginUserContext.getUserId());
        sysDictType.setCreateTime(LocalDateTime.now());
        sysDictType.setDelFlag("0");
        sysDictTypeMapper.insert(sysDictType);
        return sysDictType.getId();
    }

    private String updateById(SysDictType sysDictType) {
        // 从数据库中查询旧数据，验证code是否有修改
        SysDictType oldDictType = sysDictTypeMapper.selectById(sysDictType.getId());
        sysDictType.setUpdateId(LoginUserContext.getUserId());
        sysDictType.setUpdateTime(LocalDateTime.now());
        sysDictTypeMapper.updateById(sysDictType);
        // 当code发生修改，更新dictData表中对应的DictTypeCode值
        if (!oldDictType.getCode().equals(sysDictType.getCode())) {
            sysDictDataService.updateDataTypeCode(oldDictType.getCode(),sysDictType.getCode());
        }

        return sysDictType.getId();
    }

    @Override
    public void deleteByIds(List<String> ids) {
        checkDictData(ids);
        checkStatus(ids);
        sysDictTypeMapper.deleteBatchIds(ids);
    }

    @Override
    public String updateStatus(String id, String currentStatus) {
        UpdateWrapper<SysDictType> updateWrapper = new UpdateWrapper<>();
        String status = "0".equals(currentStatus) ? "1" : "0";

        updateWrapper.lambda()
                .set(SysDictType::getStatus, status)
                .set(SysDictType::getUpdateId, LoginUserContext.getUserId())
                .set(SysDictType::getUpdateTime, LocalDateTime.now())
                .eq(SysDictType::getId, id);
        sysDictTypeMapper.update(null, updateWrapper);
        return status;
    }

    private void checkDictData(List<String> ids) {
        List<String> dictDataIds = sysDictDataService.typeIdsToDataIds(ids);
        if (!dictDataIds.isEmpty()) {
            throw new ServiceException("存在字典数据不允许删除");
        }
    }

    private void checkStatus(List<String> ids) {
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysDictType::getId,ids)
                .eq(SysDictType::getStatus,"0");
        Long count = sysDictTypeMapper.selectCount(queryWrapper);
        if (count != 0) {
            throw new ServiceException("字典状态正常不允许删除");
        }
    }

    private void checkCode(String id,String code) {
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDictType::getCode,code);
        List<SysDictType> sysDictTypes = sysDictTypeMapper.selectList(queryWrapper);
        if (!sysDictTypes.isEmpty()) {
            if (sysDictTypes.size() > 1) {
                throw new ServiceException("当前字典类型编码已存在");
            } else {
                if (!sysDictTypes.get(0).getId().equals(id)) {
                    throw new ServiceException("当前字典类型编码已存在");
                }
            }
        }
    }
}
