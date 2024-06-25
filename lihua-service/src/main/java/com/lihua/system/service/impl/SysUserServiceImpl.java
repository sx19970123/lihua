package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.exception.ServiceException;
import com.lihua.system.entity.*;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.system.model.SysUserDTO;
import com.lihua.system.model.SysUserDeptDTO;
import com.lihua.system.model.SysUserVO;
import com.lihua.system.service.*;
import com.lihua.utils.excel.ExcelUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysUserPostService sysUserPostService;

    @Resource
    private SysUserDeptService sysUserDeptService;

    @Resource
    private SysPostService sysPostService;

    @Override
    public IPage<SysUserVO> findPage(SysUserDTO sysUserDTO) {
        IPage<SysUserVO> iPage = new Page<>(sysUserDTO.getPageNum(), sysUserDTO.getPageSize());

        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();

        // 部门id
        if (sysUserDTO.getDeptIdList() != null && !sysUserDTO.getDeptIdList().isEmpty()) {
            queryWrapper.in("dept_id", sysUserDTO.getDeptIdList());
        }
        // 昵称
        if (StringUtils.hasText(sysUserDTO.getNickname())) {
            queryWrapper.like("nickname", sysUserDTO.getNickname());
        }
        // 用户名
        if (StringUtils.hasText(sysUserDTO.getUsername())) {
            queryWrapper.like("username", sysUserDTO.getUsername());
        }
        // 电话号码
        if (StringUtils.hasText(sysUserDTO.getPhoneNumber())) {
            queryWrapper.like("phone_number", sysUserDTO.getPhoneNumber());
        }
        // 状态
        if (StringUtils.hasText(sysUserDTO.getStatus())) {
            queryWrapper.eq("status", sysUserDTO.getStatus());
        }
        // 创建时间
        if (sysUserDTO.getCreateTimeList() != null && !sysUserDTO.getCreateTimeList().isEmpty()) {
            queryWrapper.between("sys_user.create_time", sysUserDTO.getCreateTimeList().get(0),sysUserDTO.getCreateTimeList().get(1));
        }

        queryWrapper.eq("del_flag","0").orderByDesc("id");

        iPage = sysUserMapper.findPage(iPage, queryWrapper);

        if (iPage.getRecords().isEmpty()) {
            return iPage;
        }

        // 为用户所属部门赋值(一对多分页会出问题，单独处理)
        handleUserDept(iPage.getRecords());

        return iPage;
    }

    @Override
    public SysUserVO findById(String id) {
        SysUserVO sysUserVO = sysUserMapper.findById(id);
        // 设置默认部门id
        if (!sysUserVO.getDefaultDeptIdList().isEmpty()) {
            List<String> list = sysUserVO.getDefaultDeptIdList().stream().filter(StringUtils::hasText).toList();
            sysUserVO.setDefaultDeptId(!list.isEmpty() ? list.get(0) : null);
        }
        return sysUserVO;
    }

    @Override
    @Transactional
    public String save(SysUserDTO sysUserDTO) {
        SysUser sysUser = new SysUser();
        // 校验用户数据
        checkUserData(sysUserDTO);

        BeanUtils.copyProperties(sysUserDTO, sysUser);
        String id = null;

        // 插入/更新
        if (!StringUtils.hasText(sysUserDTO.getId())) {
            id = insert(sysUser);
        } else {
            id = update(sysUser);
        }

        // 保存部门数据
        saveUserDept(id, sysUserDTO.getDefaultDeptId(), sysUserDTO.getDeptIdList());
        // 保存岗位数据
        saveUserPost(id, sysUserDTO.getPostIdList());
        // 保存角色数据
        saveUserRole(id, sysUserDTO.getRoleIdList());

        return id;
    }

    @Override
    @Transactional
    public void deleteByIds(List<String> ids) {
        checkStatus(ids);
        // 删除部门、岗位、角色 与用户的关联数据
        sysUserDeptService.deleteByUserIds(ids);
        sysUserPostService.deleteByUserIds(ids);
        sysUserRoleService.deleteByUserIds(ids);
        // 删除用户信息
        sysUserMapper.deleteBatchIds(ids);
    }

    @Override
    public String updateStatus(String id, String currentStatus) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        String status = "0".equals(currentStatus) ? "1" : "0";
        updateWrapper.lambda()
                .set(SysUser::getStatus, status)
                .set(SysUser::getUpdateId, LoginUserContext.getUserId())
                .set(SysUser::getUpdateTime, LocalDateTime.now())
                .eq(SysUser::getId, id);
        sysUserMapper.update(null, updateWrapper);
        return status;
    }

    @Override
    public File exportExcel(SysUserDTO sysUserDTO) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();

        // 部门id
        if (sysUserDTO.getDeptIdList() != null && !sysUserDTO.getDeptIdList().isEmpty()) {
            queryWrapper.in("sys_dept.dept_id", sysUserDTO.getDeptIdList());
        }
        // 昵称
        if (StringUtils.hasText(sysUserDTO.getNickname())) {
            queryWrapper.like("sys_user.nickname", sysUserDTO.getNickname());
        }
        // 用户名
        if (StringUtils.hasText(sysUserDTO.getUsername())) {
            queryWrapper.like("sys_user.username", sysUserDTO.getUsername());
        }
        // 电话号码
        if (StringUtils.hasText(sysUserDTO.getPhoneNumber())) {
            queryWrapper.like("sys_user.phone_number", sysUserDTO.getPhoneNumber());
        }
        // 状态
        if (StringUtils.hasText(sysUserDTO.getStatus())) {
            queryWrapper.eq("sys_user.status", sysUserDTO.getStatus());
        }
        // 创建时间
        if (sysUserDTO.getCreateTimeList() != null && !sysUserDTO.getCreateTimeList().isEmpty()) {
            queryWrapper.between("sys_user.create_time", sysUserDTO.getCreateTimeList().get(0),sysUserDTO.getCreateTimeList().get(1));
        }

        queryWrapper.eq("sys_user.del_flag","0").orderByDesc("sys_user.id");

        List<SysUserVO> exportList = sysUserMapper.findExportData(queryWrapper);

        // 获取所有部门id
        List<String> deptIds = exportList
                .stream()
                .map(SysUserVO::getDeptIdList)
                .flatMap(Collection::stream)
                .distinct()
                .toList();

        // 根据部门id获取岗位信息
        List<SysPost> postList = sysPostService.findPostByDeptId(deptIds);

        // 创建deptId 与 post 映射
        Map<String, List<String>> postGroupByDeptId = postList
                .stream()
                .collect(Collectors.groupingBy(SysPost::getDeptId,Collectors.mapping(SysPost::getName, Collectors.toList())));
        // 根据deptId顺序设置岗位名称
        exportList.forEach(export -> {
            List<String> postNameLis = export.getDeptIdList()
                    .stream()
                    .map(deptId -> {
                        List<String> postNames = postGroupByDeptId.getOrDefault(deptId, Collections.emptyList());
                        return String.join("、", postNames);
                    }).toList();
            export.setPostLabelList(postNameLis);
        });

        // 导出excel
        return ExcelUtils.excelExport(exportList, SysUserVO.class, "系统用户");
    }

    // 处理用户所属部门
    private void handleUserDept(List<SysUserVO> records) {
        List<String> userIds = records.stream().map(SysUserVO::getId).toList();
        List<SysUserDeptDTO> userDeptByUserIds = sysUserMapper.findUserDeptByUserIds(userIds);
        Map<String, List<SysUserDeptDTO>> groupByUserId = userDeptByUserIds.stream().collect(Collectors.groupingBy(SysUserDeptDTO::getUserId));

        // 为用户所属部门赋值
        records.forEach(user -> {
            groupByUserId.forEach((key, value) -> {
                if (user.getId().equals(key)) {
                    user.setDeptLabelList(value.stream().map(SysUserDeptDTO::getDeptName).toList());
                }
            });
        });
    }

    // 新增用户
    private String insert(SysUser sysUser) {
        // 密码加密
        sysUser.setPassword(SecurityUtils.encryptPassword(sysUser.getPassword()));
        sysUser.setCreateId(LoginUserContext.getUserId());
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setDelFlag("0");
        sysUserMapper.insert(sysUser);
        return sysUser.getId();
    }

    // 更细用户信息
    private String update(SysUser sysUser) {
        sysUser.setUpdateId(LoginUserContext.getUserId());
        sysUser.setCreateTime(LocalDateTime.now());
        // 用户管理中无法更新用户密码。mp默认策略不更新null值数据
        sysUser.setPassword(null);
        sysUserMapper.updateById(sysUser);
        return sysUser.getId();
    }

    // 检查状态
    private void checkStatus(List<String> ids) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
                .in(SysUser::getId,ids)
                .eq(SysUser::getStatus,"0");
        Long count = sysUserMapper.selectCount(queryWrapper);

        if (count > 0) {
            throw new ServiceException("存在状态为正常的用户，无法删除");
        }
    }

    // 用户唯一项校验
    private void checkUserData(SysUserDTO sysUserDTO) {
        String username = sysUserDTO.getUsername();
        String phoneNumber = sysUserDTO.getPhoneNumber();
        String email = sysUserDTO.getEmail();

        List<SysUser> sysUsers = sysUserMapper.checkUserData(username, phoneNumber, email);

        // 新增或修改时username, phoneNumber, email 均唯一 校验通过
        if (sysUsers.isEmpty() || (sysUsers.size() == 1 && sysUsers.get(0).getId().equals(sysUserDTO.getId()))) {
            return;
        }

        List<String> errMessage = new ArrayList<>();
        // 用户名
        if (StringUtils.hasText(sysUserDTO.getUsername())) {
            Map<String, List<SysUser>> groupByUsername = sysUsers.stream().filter(user -> StringUtils.hasText(user.getUsername())).collect(Collectors.groupingBy(SysUser::getUsername));
            List<SysUser> byUsername = groupByUsername.getOrDefault(sysUserDTO.getUsername(),new ArrayList<>());
            if (!byUsername.isEmpty() && !byUsername.get(0).getId().equals(sysUserDTO.getId())) {
                errMessage.add("用户名");
            }
        }
        // 手机号码
        if (StringUtils.hasText(sysUserDTO.getPhoneNumber())) {
            Map<String, List<SysUser>> groupByPhoneNumber = sysUsers.stream().filter(user -> StringUtils.hasText(user.getPhoneNumber())).collect(Collectors.groupingBy(SysUser::getPhoneNumber));
            List<SysUser> byPhoneNumber = groupByPhoneNumber.getOrDefault(sysUserDTO.getPhoneNumber(),new ArrayList<>());
            if (!byPhoneNumber.isEmpty() && !byPhoneNumber.get(0).getId().equals(sysUserDTO.getId())) {
                errMessage.add("手机号码");
            }
        }
        // 邮箱
        if (StringUtils.hasText(sysUserDTO.getEmail())) {
            Map<String, List<SysUser>> groupByEmail = sysUsers.stream().filter(user -> StringUtils.hasText(user.getEmail())).collect(Collectors.groupingBy(SysUser::getEmail));
            List<SysUser> byEmail = groupByEmail.getOrDefault(sysUserDTO.getEmail(),new ArrayList<>());
            if (!byEmail.isEmpty() && !byEmail.get(0).getId().equals(sysUserDTO.getId())) {
                errMessage.add("邮箱");
            }
        }
        // 抛出异常
        if (!errMessage.isEmpty()) {
            throw new ServiceException(String.join("、", errMessage) + "已存在");
        }
    }

    // 保存用户角色关联表
    private void saveUserRole(String userId, List<String> roleIdList) {
        // 删除所有角色
        sysUserRoleService.deleteByUserIds(Collections.singletonList(userId));
        // 保存角色
        if (roleIdList != null && !roleIdList.isEmpty()) {
            // 组合数据
            List<SysUserRole> sysUserRoles = new ArrayList<>(roleIdList.size());
            LocalDateTime now = LocalDateTime.now();
            String loginUserId = LoginUserContext.getUserId();
            roleIdList.forEach(roleId -> sysUserRoles.add(new SysUserRole(userId, roleId, now, loginUserId)));
            sysUserRoleService.save(sysUserRoles);
        }
    }

    // 保存用户岗位关联表
    private void saveUserPost(String userId, List<String> postIdList) {
        // 删除所有岗位
        sysUserPostService.deleteByUserIds(Collections.singletonList(userId));

        // 保存岗位
        if (postIdList != null && !postIdList.isEmpty()) {
            // 组合数据
            List<SysUserPost> sysUserPosts  = new ArrayList<>(postIdList.size());
            LocalDateTime now = LocalDateTime.now();
            String loginUserId = LoginUserContext.getUserId();
            postIdList.forEach(postId -> sysUserPosts.add(new SysUserPost(userId, postId, now, loginUserId)));
            sysUserPostService.save(sysUserPosts);
        }
    }

    // 保存用户部门关联表
    private void saveUserDept(String userId, String defaultDeptId, List<String> deptIdList) {
        // 删除所有部门
        sysUserDeptService.deleteByUserIds(Collections.singletonList(userId));
        // 保存部门
        if (deptIdList != null && !deptIdList.isEmpty()) {
            // 组合数据
            List<SysUserDept> sysUserDeptList  = new ArrayList<>(deptIdList.size());
            LocalDateTime now = LocalDateTime.now();
            String loginUserId = LoginUserContext.getUserId();
            deptIdList.forEach(deptId -> sysUserDeptList.add(new SysUserDept(userId, deptId, now, loginUserId, deptId.equals(defaultDeptId) ? "0" : "1")));

            sysUserDeptService.save(sysUserDeptList);
        }

    }

}
