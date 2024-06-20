package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.exception.ServiceException;
import com.lihua.system.entity.SysDept;
import com.lihua.system.entity.SysMenu;
import com.lihua.system.entity.SysPost;
import com.lihua.system.mapper.SysDeptMapper;
import com.lihua.system.model.SysDeptVO;
import com.lihua.system.service.SysDeptService;
import com.lihua.system.service.SysPostService;
import com.lihua.system.service.SysUserDeptService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.tree.TreeUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysPostService sysPostService;

    @Resource
    private SysUserDeptService sysUserDeptService;

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

        return sysDeptMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysDeptVO> findDeptPostList(SysDept sysDept) {
        // 查询 dept 数据
        List<SysDept> sysDeptList = findList(sysDept);

        if (sysDeptList.isEmpty()) {
            return new ArrayList<>();
        }

        List<SysDeptVO> sysDeptVOS = new ArrayList<>();

        // 根据dept id 查询岗位数据
        List<String> deptIds = sysDeptList.stream().map(SysDept::getId).toList();
        List<SysPost> postByDeptIdList = sysPostService.findPostByDeptId(deptIds);

        // 部门岗位数据组合
        Map<String, List<SysPost>> postListGroupByDeptId =
                postByDeptIdList.stream().collect(Collectors.groupingBy(SysPost::getDeptId));

        sysDeptList.forEach(dept -> {
            SysDeptVO sysDeptVO = new SysDeptVO();
            BeanUtils.copyProperties(dept,sysDeptVO);
            if (postListGroupByDeptId.get(dept.getId()) != null) {
                sysDeptVO.setSysPostList(postListGroupByDeptId.get(dept.getId()));
            }
            sysDeptVOS.add(sysDeptVO);
        });

        // 构建树返回
        return TreeUtils.buildTree(sysDeptVOS);
    }

    @Override
    public String save(SysDept sysDept) {
        checkDeptCode(sysDept);
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
        List<SysDept> list = findList(new SysDept());
        return TreeUtils.buildTree(list);
    }

    @Override
    public String updateStatus(String id, String currentStatus) {
        UpdateWrapper<SysDept> updateWrapper = new UpdateWrapper<>();
        String status = "0".equals(currentStatus) ? "1" : "0";

        updateWrapper.lambda()
                .set(SysDept::getStatus, status)
                .set(SysDept::getUpdateId, LoginUserContext.getUserId())
                .set(SysDept::getUpdateTime, LocalDateTime.now())
                .eq(SysDept::getId, id);
        sysDeptMapper.update(null, updateWrapper);
        return status;
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
        queryWrapper.lambda()
                .in(SysDept::getParentId,ids)
                .select(SysDept::getId);
        List<SysDept> sysDepts = sysDeptMapper.selectList(queryWrapper);

        if (sysDepts.isEmpty()) {
            return;
        }

        // 对比以删除节点为父节点的数据，当这些数据全部与删除的数据相同，则要删除的数据中没有子节点存在
        List<String> list = new java.util.ArrayList<>(sysDepts.stream().map(SysDept::getId).toList());
        list.removeAll(ids);

        if (!list.isEmpty()) {
            throw new ServiceException("存在子集不允许删除");
        }
    }

    // 验证是否存在岗位
    private void checkPost(List<String> ids) {
        Long count = sysPostService.findCountByDeptId(ids);
        if (count > 0) {
            throw new ServiceException("存在岗位不允许删除");
        }
    }

    // 验证是否关联了用户
    private void checkUser(List<String> ids) {
        Long count = sysDeptMapper.deptUserCount(ids);
        if (count > 0) {
            throw new ServiceException("存在用户不允许删除");
        }
    }

    // 校验dept code唯一性
    private void checkDeptCode(SysDept sysDept) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDept::getCode,sysDept.getCode());
        List<SysDept> sysDeptList = sysDeptMapper.selectList(queryWrapper);

        if (sysDeptList.isEmpty()) {
            return;
        }

        if (sysDeptList.size() > 1) {
            throw new ServiceException("单位编码已存在");
        }

        if (!sysDeptList.get(0).getId().equals(sysDept.getId())) {
            throw new ServiceException("单位编码已存在");
        }
    }
}
