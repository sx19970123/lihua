package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.system.entity.SysDictType;
import com.lihua.system.mapper.SysDictTypeMapper;
import com.lihua.system.model.SysDictTypeDTO;
import com.lihua.system.service.SysDictTypeService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysDictTypeServiceImpl implements SysDictTypeService {

    @Resource
    private SysDictTypeMapper sysDictTypeMapper;

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
    public String save(SysDictType sysDictType) {
        if (!StringUtils.hasText(sysDictType.getId())) {
            return insert(sysDictType);
        }
        return updateById(sysDictType);
    }

    private String insert(SysDictType sysDictType) {
        sysDictType.setCreateId(LoginUserContext.getUserId());
        sysDictType.setCreateTime(LocalDateTime.now());
        sysDictTypeMapper.insert(sysDictType);
        return sysDictType.getId();
    }

    private String updateById(SysDictType sysDictType) {
        sysDictType.setUpdateId(LoginUserContext.getUserId());
        sysDictType.setUpdateTime(LocalDateTime.now());
        sysDictTypeMapper.updateById(sysDictType);
        return sysDictType.getId();
    }

    @Override
    public void deleteByIds(List<String> ids) {
        sysDictTypeMapper.deleteBatchIds(ids);
    }
}
