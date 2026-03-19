package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.collection.CollectionUtils;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.*;
import com.lihua.excel.exception.ExcelImportException;
import com.lihua.mapper.SysDeptMapper;
import com.lihua.mapper.SysRoleMapper;
import com.lihua.mapper.SysUserMapper;
import com.lihua.model.dto.ResetPasswordDTO;
import com.lihua.model.dto.SysUserDTO;
import com.lihua.model.dto.SysUserDeptDTO;
import com.lihua.model.vo.SysPostVO;
import com.lihua.model.vo.SysUserVO;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.security.utils.SecurityUtils;
import com.lihua.sensitive.annotation.ApplySensitive;
import com.lihua.service.*;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>  implements SysUserService {

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

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysSettingService sysSettingService;

    // 校验手机号码
    private final String PHONE_NUMBER_PATTERN = "^1[3-9]\\d{9}$";
    // 校验邮箱
    private final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @ApplySensitive
    @Override
    public IPage<SysUserVO> queryPage(SysUserDTO sysUserDTO) {
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
        List<LocalDate> createTimeList = sysUserDTO.getCreateTimeList();
        if (createTimeList != null && createTimeList.size() == 2) {
            queryWrapper.between("sys_user.create_time", createTimeList.get(0),createTimeList.get(1).plusDays(1L));
        }

        queryWrapper.eq("del_flag","0").orderByDesc("sys_user.create_time");

        iPage = sysUserMapper.queryPage(iPage, queryWrapper);

        if (iPage.getRecords().isEmpty()) {
            return iPage;
        }

        // 为用户所属部门赋值(一对多分页会出问题，单独处理)
        handleUserDept(iPage.getRecords());

        return iPage;
    }

    @Override
    public SysUserVO queryById(String id) {
        SysUserVO sysUserVO = sysUserMapper.queryById(id);
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
        String id;

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
        sysUserMapper.deleteByIds(ids);
    }

    @Override
    public String updateStatus(String id, String currentStatus) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        String status = "0".equals(currentStatus) ? "1" : "0";
        updateWrapper.lambda()
                .set(SysUser::getStatus, status)
                .set(SysUser::getUpdateId, LoginUserContext.getUserId())
                .set(SysUser::getUpdateTime, DateUtils.now())
                .eq(SysUser::getId, id);
        sysUserMapper.update(null, updateWrapper);
        return status;
    }

    @ApplySensitive
    @Override
    public List<SysUserVO> exportExcel(SysUserDTO sysUserDTO) {
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

        // 数据查询
        List<SysUserVO> sysUserVOS = sysUserMapper.queryExportData(queryWrapper);

        // 根据用户id查询用户拥有的岗位，并根据部门id分组，获取部门id下对应的岗位集合
        Set<String> userIdSet = sysUserVOS.stream().map(SysUserVO::getId).collect(Collectors.toSet());
        List<SysPostVO> sysPosts = sysUserPostService.queryPostByUserIds(userIdSet);
        Map<String, List<SysPostVO>> groupByDeptIdPostMap = sysPosts
                .stream()
                .collect(Collectors.groupingBy(SysPost::getDeptId));

        // 处理拼接角色名称
        return sysUserVOS
                .stream()
                .peek(sysUserVO -> {
                    // 角色名称
                    sysUserVO.setRoleName(String.join("、", sysUserVO.getRoleNameList()));
                    // 用户拥有的岗位
                    List<SysPostVO> sysPostVOS = groupByDeptIdPostMap.get(sysUserVO.getDeptId());
                    if (sysPostVOS != null && !sysPostVOS.isEmpty()) {
                        Set<String> postNames = sysPostVOS
                                .stream()
                                .filter(post -> post.getUserId().equals(sysUserVO.getId()))
                                .map(SysPostVO::getName)
                                .collect(Collectors.toSet());
                        sysUserVO.setPostName(String.join("、", postNames));
                    }
                })
                .toList();
    }

    @Transactional
    @Override
    public void importExcel(List<SysUser> sysUserList) {
        String defaultPassword = sysSettingService.getDefaultPassword();
        if (!StringUtils.hasText(defaultPassword)) {
            throw new ExcelImportException("请联系管理员配置默认密码");
        }

        // 进行数据格式、重复项等检查
        checkImportData(sysUserList);

        LocalDateTime now = DateUtils.now();
        // 批量插入
        sysUserList.forEach(sysUser -> {
            sysUser.setRegisterType("2");
            sysUser.setPassword(SecurityUtils.encryptPassword(defaultPassword));
            sysUser.setPasswordUpdateTime(now);
        });

        saveBatch(sysUserList);
    }

    @Override
    public List<SysUser> userOption(String deptId) {
        return sysUserMapper.queryOptionByDeptId(deptId);
    }

    @Override
    public List<SysUser> userOption(List<String> userIdList) {
        return sysUserMapper.queryOptionByUserIds(userIdList);
    }

    @Override
    public List<String> queryAllUserIds() {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(SysUser::getId)
                .eq(SysUser::getDelFlag, "0");
        return sysUserMapper.selectList(queryWrapper).stream().map(SysUser::getId).toList();
    }

    @Override
    public String resetPassword(ResetPasswordDTO resetPasswordDTO) {
        LocalDateTime now = DateUtils.now();
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                        .eq(SysUser::getId, resetPasswordDTO.getUserId())
                        .set(SysUser::getPassword, SecurityUtils.encryptPassword(resetPasswordDTO.getPassword()))
                        .set(SysUser::getPasswordUpdateTime, now)
                        .set(SysUser::getUpdateId, LoginUserContext.getUserId())
                        .set(SysUser::getUpdateTime, now);
        sysUserMapper.update(updateWrapper);
        return resetPasswordDTO.getUserId();
    }


    // 处理用户所属部门
    private void handleUserDept(List<SysUserVO> records) {
        List<String> userIds = records.stream().map(SysUserVO::getId).toList();
        List<SysUserDeptDTO> userDeptByUserIds = sysUserMapper.queryUserDeptByUserIds(userIds);
        Map<String, List<SysUserDeptDTO>> groupByUserId = userDeptByUserIds.stream().collect(Collectors.groupingBy(SysUserDeptDTO::getUserId));

        // 为用户所属部门赋值
        records.forEach(user -> groupByUserId.forEach((key, value) -> {
            if (user.getId().equals(key)) {
                user.setDeptLabelList(value.stream().map(SysUserDeptDTO::getDeptName).toList());
            }
        }));
    }

    // 新增用户
    private String insert(SysUser sysUser) {

        LocalDateTime now = DateUtils.now();
        // 密码加密
        sysUser.setPassword(SecurityUtils.encryptPassword(sysUser.getPassword()));
        sysUser.setRegisterType("0");
        sysUser.setPasswordUpdateTime(now);
        sysUserMapper.insert(sysUser);
        return sysUser.getId();
    }

    // 更细用户信息
    private String update(SysUser sysUser) {
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
        String userId = sysUserDTO.getId();
        // 查询出与修改后信息冲突的用户
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(userId)) {
            queryWrapper.ne(SysUser::getId, userId);
        }
        queryWrapper
            .and(wrapper -> {
                wrapper.eq(SysUser::getUsername, username);
                if (StringUtils.hasText(email)) {
                    wrapper.or().eq(SysUser::getEmail, email);
                }
                if (StringUtils.hasText(phoneNumber)) {
                    wrapper.or().eq(SysUser::getPhoneNumber, phoneNumber);
                }
            })
            .eq(SysUser::getDelFlag, "0");

        // 用于保存冲突字段的集合
        Set<String> conflictingMsgSet = new LinkedHashSet<>();

        // 执行查询
        List<SysUser> conflictingUsers = sysUserMapper.selectList(queryWrapper);
        // 遍历冲突用户，确定具体冲突的字段
        for (SysUser user : conflictingUsers) {
            if (StringUtils.hasText(username) && username.equals(user.getUsername())) {
                conflictingMsgSet.add("用户名");
            }
            if (StringUtils.hasText(phoneNumber) && phoneNumber.equals(user.getPhoneNumber())) {
                conflictingMsgSet.add("手机号码");
            }
            if (StringUtils.hasText(email) && email.equals(user.getEmail())) {
                conflictingMsgSet.add("邮箱");
            }
        }

        // 可能存在多项冲突，一次性全部返回
        if (!conflictingMsgSet.isEmpty()) {
            throw new ServiceException(String.join("、", conflictingMsgSet) + "已存在");
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
            LocalDateTime now = DateUtils.now();
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
            LocalDateTime now = DateUtils.now();
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
            LocalDateTime now = DateUtils.now();
            String loginUserId = LoginUserContext.getUserId();
            deptIdList.forEach(deptId -> sysUserDeptList.add(new SysUserDept(userId, deptId, now, loginUserId, deptId.equals(defaultDeptId) ? "0" : "1")));

            sysUserDeptService.save(sysUserDeptList);
        }

    }

    /**
     * 数据导入检查
     */
    private void checkImportData(List<SysUser> sysUserList) {
        if (sysUserList.isEmpty()) {
            throw new ExcelImportException("数据为空");
        }

        List<String> importErrMssageList = new ArrayList<>();

        // 记录重复数据
        List<String> usernameList = new ArrayList<>();
        List<String> phonenumberList = new ArrayList<>();
        List<String> emailList = new ArrayList<>();

        // 必填｜格式 校验
        for (int i = 0; i < sysUserList.size(); i++) {
            SysUser sysUser = sysUserList.get(i);
            List<String> errItemList = new ArrayList<>();

            String username = sysUser.getUsername();
            if (!StringUtils.hasText(username)) {
                errItemList.add("用户名为空");
            } else {
                usernameList.add(username);
            }

            String nickname = sysUser.getNickname();
            if (!StringUtils.hasText(nickname)) {
                errItemList.add("昵称为空");
            }

            String gender = sysUser.getGender();
            if (!StringUtils.hasText(gender)) {
                errItemList.add("性别为空");
            }

            String status = sysUser.getStatus();
            if (!StringUtils.hasText(status)) {
                errItemList.add("状态为空");
            }

            String email = sysUser.getEmail();
            if (StringUtils.hasText(email)) {
                boolean matches = email.matches(EMAIL_PATTERN);
                if (!matches) {
                    errItemList.add("邮箱格式不正确");
                } else {
                    emailList.add(email);
                }
            }

            String phoneNumber = sysUser.getPhoneNumber();
            if (StringUtils.hasText(phoneNumber)) {
                boolean matches = phoneNumber.matches(PHONE_NUMBER_PATTERN);
                if (!matches) {
                    errItemList.add("手机号码格式不正确");
                } else {
                    phonenumberList.add(phoneNumber);
                }
            }

            // 汇总错误信息
            if (!errItemList.isEmpty()) {
                errItemList.set(0, "第" + (i + 1) + "行：" + errItemList.get(0));
                importErrMssageList.add(String.join("，", errItemList));
            }
        }

        // 必填｜格式校验不通过，抛出异常
        if (!importErrMssageList.isEmpty()) {
            throw new ExcelImportException(importErrMssageList);
        }

        // 获取单元格内重复的字段
        Set<String> usernameSet = CollectionUtils.getRepeatItem(usernameList);
        Set<String> phonenumberSet = CollectionUtils.getRepeatItem(phonenumberList);
        Set<String> emailSet = CollectionUtils.getRepeatItem(emailList);

        // 重复的用户名
        if (!usernameSet.isEmpty()) {
            importErrMssageList.add("重复用户名：" + String.join(",", usernameSet));
        }

        // 重复的手机号
        if (!phonenumberSet.isEmpty()) {
            importErrMssageList.add("重复手机号：" + String.join(",", phonenumberSet));
        }

        // 重复的邮箱地址
        if (!emailSet.isEmpty()) {
            importErrMssageList.add("重复邮箱：" + String.join(",", String.join(",", emailSet)));
        }

        // 单元格内存在重复数据，抛出异常
        if (!importErrMssageList.isEmpty()) {
            throw new ExcelImportException(importErrMssageList);
        }

        // 用户名不为空
        if (!usernameList.isEmpty()) {
            List<String> dbUsernameList = lambdaQuery().select(SysUser::getUsername).in(SysUser::getUsername, usernameList).list().stream().map(SysUser::getUsername).toList();
            if (!dbUsernameList.isEmpty()) {
                importErrMssageList.add("数据库已存在的用户名：" + String.join(",", dbUsernameList));
            }
        }

        // 手机号不为空
        if (!phonenumberList.isEmpty()) {
            List<String> dbPhonenumberList = lambdaQuery().select(SysUser::getPhoneNumber).in(SysUser::getPhoneNumber, phonenumberList).list().stream().map(SysUser::getPhoneNumber).toList();
            if (!dbPhonenumberList.isEmpty()) {
                importErrMssageList.add("数据库已存在的手机号：" + String.join(",", dbPhonenumberList));
            }
        }

        // 邮箱不为空
        if (!emailList.isEmpty()) {
            List<String> dbEmailList = lambdaQuery().select(SysUser::getEmail).in(SysUser::getEmail, emailList).list().stream().map(SysUser::getEmail).toList();
            if (!dbEmailList.isEmpty()) {
                importErrMssageList.add("数据库已存在的邮箱：" + String.join(",", dbEmailList));
            }
        }

        // 数据库中存在的数据，抛出异常
        if (!importErrMssageList.isEmpty()) {
            throw new ExcelImportException(importErrMssageList);
        }
    }

}
