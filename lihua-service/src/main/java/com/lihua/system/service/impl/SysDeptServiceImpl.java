package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.system.entity.SysDept;
import com.lihua.system.mapper.SysDeptMapper;
import com.lihua.system.service.SysDeptService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.tree.TreeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public List<SysDept> findList(SysDept sysDept) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        // 单位名称
        if (StringUtils.hasText(sysDept.getName())) {
            queryWrapper.lambda().like(SysDept::getName,sysDept.getName());
        }
        if (StringUtils.hasText(sysDept.getCode())) {
            queryWrapper.lambda().like(SysDept::getCode,sysDept.getCode());
        }

        List<SysDept> sysDeptList = sysDeptMapper.selectList(queryWrapper);
        return TreeUtil.buildTree(sysDeptList);
    }

    @Override
    public String save(SysDept sysDept) {
        if (StringUtils.hasText(sysDept.getId())) {
            return updateById(sysDept);
        } else {
            return insert(sysDept);
        }
    }

    private String updateById(SysDept sysDept) {
        sysDept.setUpdateId(LoginUserContext.getUserId());
        sysDept.setUpdateTime(LocalDateTime.now());
        sysDeptMapper.updateById(sysDept);
        return sysDept.getId();
    }

    private String insert(SysDept sysDept) {
        sysDept.setCreateId(LoginUserContext.getUserId());
        sysDept.setCreateTime(LocalDateTime.now());
        sysDept.setDelFlag("0");
        sysDeptMapper.insert(sysDept);
        return sysDept.getId();
    }

    @Override
    public SysDept findById(String id) {
        return sysDeptMapper.selectById(id);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        sysDeptMapper.deleteBatchIds(ids);
    }

    @Override
    public List<SysDept> deptTreeOption() {
        return findList(new SysDept());
    }
}
