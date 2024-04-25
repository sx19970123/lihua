package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.exception.ServiceException;
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
        // 部门名称
        if (StringUtils.hasText(sysDept.getName())) {
            queryWrapper.lambda().like(SysDept::getName,sysDept.getName());
        }
        // 部门编码
        if (StringUtils.hasText(sysDept.getCode())) {
            queryWrapper.lambda().like(SysDept::getCode,sysDept.getCode());
        }
        // 状态
        if (StringUtils.hasText(sysDept.getStatus())) {
            queryWrapper.lambda().eq(SysDept::getStatus,sysDept.getStatus());
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
        // 数据是否为停用状态
        checkStatus(ids);
        // 数据没有子集
        checkChildren(ids);
        // 数据下没有岗位
        checkPost(ids);
        // 数据下没有分配用户
        checkUser(ids);

        sysDeptMapper.deleteBatchIds(ids);
    }

    @Override
    public List<SysDept> deptTreeOption() {
        return findList(new SysDept());
    }

    // 检查状态
    private void checkStatus(List<String> ids) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysDept::getStatus,"0")
                .in(SysDept::getId,ids);
        Long count = sysDeptMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("部门状态为正常不允许删除");
        }
    }

    // 验证是否存在子集
    private void checkChildren(List<String> ids) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysDept::getParentId,ids);
        Long count = sysDeptMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("存在子集不允许删除");
        }
    }
    private void checkPost(List<String> ids) {
    }

    // 验证是否关联了用户
    private void checkUser(List<String> ids) {
        Long count = sysDeptMapper.deptUserCount(ids);
        if (count > 0) {
            throw new ServiceException("存在用户不允许删除");
        }
    }
}
