package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.common.utils.tree.TreeUtils;
import com.lihua.entity.SysDept;
import com.lihua.entity.SysPost;
import com.lihua.mapper.SysDeptMapper;
import com.lihua.model.vo.SysDeptVO;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.service.SysDeptService;
import com.lihua.service.SysPostService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysPostService sysPostService;

    @Override
    public List<SysDept> queryList(SysDept sysDept) {
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
        queryWrapper.lambda().orderByAsc(SysDept::getSort);
        return sysDeptMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysDeptVO> queryDeptPostList(SysDept sysDept) {
        // 查询 dept 数据
        List<SysDept> sysDeptList = queryList(sysDept);

        if (sysDeptList.isEmpty()) {
            return new ArrayList<>();
        }

        List<SysDeptVO> sysDeptVOS = new ArrayList<>();

        // 根据dept id 查询岗位数据
        List<String> deptIds = sysDeptList.stream().map(SysDept::getId).toList();
        List<SysPost> postByDeptIdList = sysPostService.queryPostByDeptId(deptIds);

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

        return sysDeptVOS;
    }

    @Override
    public String saveDept(SysDept sysDept) {
        checkDeptCode(sysDept);
        checkDeptName(sysDept);
        if (StringUtils.hasText(sysDept.getId())) {
            return update(sysDept);
        } else {
            return insert(sysDept);
        }
    }

    private String update(SysDept sysDept) {
        sysDeptMapper.updateById(sysDept);
        return sysDept.getId();
    }

    private String insert(SysDept sysDept) {
        sysDeptMapper.insert(sysDept);
        return sysDept.getId();
    }

    @Override
    public SysDept queryById(String id) {
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

        sysDeptMapper.deleteByIds(ids);
    }

    @Override
    public List<SysDept> deptTreeOption() {
        List<SysDept> list = queryList(new SysDept());
        return TreeUtils.buildTree(list);
    }

    @Override
    public String updateStatus(String id, String currentStatus) {
        UpdateWrapper<SysDept> updateWrapper = new UpdateWrapper<>();
        String status = "0".equals(currentStatus) ? "1" : "0";

        updateWrapper.lambda()
                .set(SysDept::getStatus, status)
                .set(SysDept::getUpdateId, LoginUserContext.getUserId())
                .set(SysDept::getUpdateTime, DateUtils.now())
                .eq(SysDept::getId, id);
        sysDeptMapper.update(null, updateWrapper);
        return status;
    }

    @Override
    public List<SysDeptVO> exportExcel(SysDept sysDept) {
        // 查询部门岗位信息
        List<SysDeptVO> deptPostList = queryDeptPostList(sysDept);
        // 处理生成路径名称和岗位名称拼接
        deptPostList.forEach(post -> {
            // 路径名称
            String namePath = getNamePath(deptPostList, post.getId());
            post.setNamePath(namePath);
            // 岗位名称
            if (post.getSysPostList() != null && !post.getSysPostList().isEmpty()) {
                List<String> postNameList = post.getSysPostList().stream().map(SysPost::getName).toList();
                post.setPostNames(String.join("、", postNameList));
            }
        });
        return deptPostList;
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
        List<String> list = new ArrayList<>(sysDepts.stream().map(SysDept::getId).toList());
        list.removeAll(ids);

        if (!list.isEmpty()) {
            throw new ServiceException("存在子集不允许删除");
        }
    }

    // 验证是否存在岗位
    private void checkPost(List<String> ids) {
        Long count = sysPostService.queryCountByDeptId(ids);
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
            throw new ServiceException("部门编码已存在");
        }

        if (!sysDeptList.get(0).getId().equals(sysDept.getId())) {
            throw new ServiceException("部门编码已存在");
        }
    }

    private void checkDeptName(SysDept sysDept) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDept::getName,sysDept.getName());
        List<SysDept> sysDeptList = sysDeptMapper.selectList(queryWrapper);

        if (sysDeptList.isEmpty()) {
            return;
        }

        if (sysDeptList.size() > 1) {
            throw new ServiceException("部门名称已存在");
        }

        if (!sysDeptList.get(0).getId().equals(sysDept.getId())) {
            throw new ServiceException("部门名称已存在");
        }
    }

    // 获取名称路径
    private String getNamePath(List<SysDeptVO> sysDeptList, String deptId) {
        List<SysDeptVO> targetSingleList = sysDeptList.stream().filter(dept -> dept.getId().equals(deptId)).toList();
        if (!targetSingleList.isEmpty()) {
            // 获取到最末级（自己）
            SysDeptVO sysDept = targetSingleList.get(0);
            String name = sysDept.getName();
            //  当前pid为 0 即为顶级部门，名称路径为自己部门名称
            if ("0".equals(sysDept.getParentId())) {
                return name;
            } else {
                String parentName = getNamePath(sysDeptList, sysDept.getParentId());
                return parentName + "/" + name;
            }
        }
        return null;
    }
}
