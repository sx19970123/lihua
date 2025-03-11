package com.lihua.service.system.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.annotation.sensitive.ApplySensitive;
import com.lihua.entity.system.*;
import com.lihua.exception.ServiceException;
import com.lihua.model.dict.SysDictDataVO;
import com.lihua.model.excel.ExcelImportResult;
import com.lihua.service.system.post.SysPostService;
import com.lihua.service.system.setting.SysSettingService;
import com.lihua.mapper.system.SysDeptMapper;
import com.lihua.mapper.system.SysRoleMapper;
import com.lihua.mapper.system.SysUserMapper;
import com.lihua.model.system.dto.ResetPasswordDTO;
import com.lihua.model.system.dto.SysUserDTO;
import com.lihua.model.system.dto.SysUserDeptDTO;
import com.lihua.model.system.vo.SysDeptVO;
import com.lihua.model.system.vo.SysUserVO;
import com.lihua.service.system.user.SysUserDeptService;
import com.lihua.service.system.user.SysUserPostService;
import com.lihua.service.system.user.SysUserRoleService;
import com.lihua.service.system.user.SysUserService;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.dict.DictUtils;
import com.lihua.utils.excel.ExcelUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
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
    private final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    // 校验邮箱
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

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
            // 对密码进行解密处理
            sysUser.setPassword(SecurityUtils.decryptGetPassword(sysUserDTO.getPassword(), sysUserDTO.getPasswordRequestKey()));
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

    @Override
    public String exportExcel(SysUserDTO sysUserDTO) {
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

        List<SysUserVO> exportList = sysUserMapper.queryExportData(queryWrapper);

        // 获取所有部门id
        List<String> deptIds = exportList
                .stream()
                .map(SysUserVO::getDeptIdList)
                .flatMap(Collection::stream)
                .distinct()
                .toList();

        // 根据部门id获取岗位信息
        List<SysPost> postList = sysPostService.queryPostByDeptId(deptIds);

        // 创建deptId 与 post 映射
        Map<String, List<String>> postGroupByDeptId = postList
                .stream()
                .collect(Collectors.groupingBy(SysPost::getDeptId,Collectors.mapping(SysPost::getName, Collectors.toList())));

        exportList.forEach(export -> {
            // 根据deptId顺序设置岗位名称
            List<String> postNameLis = export.getDeptIdList()
                    .stream()
                    .map(deptId -> {
                        List<String> postNames = postGroupByDeptId.getOrDefault(deptId, Collections.emptyList());
                        return String.join("、", postNames);
                    }).toList();
            export.setPostLabelList(postNameLis);
            // 角色名称
            export.setRoleName(String.join("、",export.getRoleNameList()));
        });

        // 导出excel
        return ExcelUtils.excelExport(exportList, SysUserVO.class, "系统用户");
    }

    @Transactional
    @Override
    public ExcelImportResult importExcel(List<SysUserVO> sysUserVOS) {
        String defaultPassword = sysSettingService.getDefaultPassword();
        if ("".equals(defaultPassword)) {
            throw new ServiceException("请联系管理员配置默认密码");
        }

        // 无法倒入的用户列表
        List<SysUserVO> errorUserVos = new ArrayList<>();
        // 可倒入的用户列表
        List<SysUserVO> importUserVos;


        // 记录到的重复数据：用户名/电话号码/邮箱
        Set<String> usernameRepeatSet = new HashSet<>();
        Set<String> phoneNumberRepeatSet = new HashSet<>();
        Set<String> emailRepeatSet = new HashSet<>();
        // 记录重复的数据
        recordRepeatData(sysUserVOS, usernameRepeatSet, phoneNumberRepeatSet, emailRepeatSet);


        // 从数据库查询出的数据：用户名/手机号码/邮箱
        Set<String> usernameDbSet = new HashSet<>();
        Set<String> phoneNumberDbSet = new HashSet<>();
        Set<String> emailDbSet = new HashSet<>();
        // 从数据库查询对应用户名
        getDbUserData(sysUserVOS, usernameDbSet, phoneNumberDbSet, emailDbSet);


        // 获取需要的字典数据
        List<SysDictDataVO> sysStatus = DictUtils.getDictData("sys_status");
        List<SysDictDataVO> userGender = DictUtils.getDictData("user_gender");
        String genderJoin = userGender.stream().map(SysDictDataVO::getLabel).collect(Collectors.joining("、"));
        String statusJoin = sysStatus.stream().map(SysDictDataVO::getLabel).collect(Collectors.joining("、"));


        // 获取需要的角色数据
        List<SysRole> roleList = sysRoleMapper.queryAllRole();
        List<String> allRoleNameList = roleList.stream().map(SysRole::getName).toList();


        // 获取需要的部门/岗位数据
        List<SysDeptVO> sysDeptList = sysDeptMapper.queryAllDept();
        List<String> allDeptNameList = sysDeptList.stream().map(SysDept::getName).toList();

        // 开始过滤数据，返回false的数据将存储到 errorUserVos
        importUserVos = sysUserVOS.stream().filter(sysUserVO -> {

            // 过滤excel表格内的重复数据（用户名/手机号码/邮箱）
            boolean filterRepeatData = filterRepeatData(sysUserVO, errorUserVos, usernameRepeatSet, phoneNumberRepeatSet, emailRepeatSet);
            if (!filterRepeatData) {
                return false;
            }

            // 过滤excel表格内不符合的用户名（已存在、未填写）
            boolean filterUsername = filterUsername(sysUserVO, errorUserVos, usernameDbSet);
            if (!filterUsername) {
                return false;
            }

            // 过滤excel表格内不合符的手机号码（已存在、格式不符合）
            boolean filterPhoneNumber = filterPhoneNumber(sysUserVO, errorUserVos, phoneNumberDbSet);
            if (!filterPhoneNumber) {
                return false;
            }

            // 过滤excel表格内不合符的邮箱（已存在、格式不符合）
            boolean filterEmail = filterEmail(sysUserVO, errorUserVos, emailDbSet);
            if (!filterEmail) {
                return false;
            }

            // 过滤掉excel表格中不符合的用户昵称（未填写）
            boolean filterNickname = filterNickname(sysUserVO, errorUserVos);
            if (!filterNickname) {
                return false;
            }

            // 过滤掉excel表格中不符合的字典数据（label不存在、未填写），符合条件的字典label将被替换为value
            boolean filterDictData = filterDictData(sysUserVO, errorUserVos, userGender, sysStatus, genderJoin, statusJoin);
            if (!filterDictData) {
                return false;
            }

            // 过滤掉excel表格中不符合的角色（admin、系统不存在的数据）
            boolean filterRole = filterRole(sysUserVO, errorUserVos, allRoleNameList);
            if (!filterRole) {
                return false;
            }

            // 过滤掉excel表格中不符合的部门（系统不存在的数据）
            boolean filterDept = filterDept(sysUserVO, errorUserVos,allDeptNameList);
            if (!filterDept) {
                return false;
            }

            // 过滤掉excel表格中不符合的岗位（数量不匹配、系统不存在的数据）
            return filterPost(sysUserVO, errorUserVos, sysDeptList);
        }).toList();

        // 处理完毕后获得两批数据：通过校验可导入 / 数据存在异常需用户重新处理
        // 导出错误数据集
        String errExcelPath = null;
        if (!errorUserVos.isEmpty()) {
            String errExcelName = LoginUserContext.getUserId() + "_导入失败_" + UUID.randomUUID().toString().replace("-","");
            // 导出excel
            errExcelPath = ExcelUtils.excelExport(errorUserVos, SysUserVO.class, errExcelName);
        }
        // 插入数据
        if (!importUserVos.isEmpty()) {
            batchInsert(importUserVos, roleList, sysDeptList);
        }

        // 返回汇总的导入结果
        return new ExcelImportResult(sysUserVOS.size() == importUserVos.size(),
                sysUserVOS.size(),
                importUserVos.size(),
                errorUserVos.size(),
                errExcelPath);
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
        // 解密获取密码明文
        String password = SecurityUtils.decryptGetPassword(resetPasswordDTO.getPassword(), resetPasswordDTO.getPasswordRequestKey());

        LocalDateTime now = DateUtils.now();
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                        .eq(SysUser::getId, resetPasswordDTO.getUserId())
                        .set(SysUser::getPassword, SecurityUtils.encryptPassword(password))
                        .set(SysUser::getPasswordUpdateTime, now)
                        .set(SysUser::getUpdateId, LoginUserContext.getUserId())
                        .set(SysUser::getUpdateTime, now);
        sysUserMapper.update(updateWrapper);
        return resetPasswordDTO.getUserId();
    }

    /**
     * 循环 sysUserVOS 记录重复数据
     */
    private void recordRepeatData(List<SysUserVO> sysUserVOS, Set<String> usernameRepeatSet, Set<String> phoneNumberRepeatSet, Set<String> emailRepeatSet) {
        // 去除列表中重复的数据（用户名/手机号码/邮箱）
        Map<String, Integer> usernameCountMap = new HashMap<>();
        Map<String, Integer> phoneNumberCountMap = new HashMap<>();
        Map<String, Integer> emailCountMap = new HashMap<>();

        sysUserVOS.forEach(sysUserVO -> {
            usernameCountMap.put(sysUserVO.getUsername(), usernameCountMap.getOrDefault(sysUserVO.getUsername(),0) + 1);
            if (usernameCountMap.get(sysUserVO.getUsername()) > 1) {
                usernameRepeatSet.add(sysUserVO.getUsername());
            }

            if (StringUtils.hasText(sysUserVO.getPhoneNumber())) {
                phoneNumberCountMap.put(sysUserVO.getPhoneNumber(), phoneNumberCountMap.getOrDefault(sysUserVO.getPhoneNumber(),0) + 1);
                if (phoneNumberCountMap.get(sysUserVO.getPhoneNumber()) > 1) {
                    phoneNumberRepeatSet.add(sysUserVO.getPhoneNumber());
                }
            }

            if (StringUtils.hasText(sysUserVO.getEmail())) {
                emailCountMap.put(sysUserVO.getEmail(), emailCountMap.getOrDefault(sysUserVO.getEmail(),0) + 1);
                if (emailCountMap.get(sysUserVO.getEmail()) > 1) {
                    emailRepeatSet.add(sysUserVO.getEmail());
                }
            }
        });
    }

    /**
     * 获取全部用户名
     */
    private void getDbUserData(List<SysUserVO> sysUserVOS,
                                       Set<String> usernameDbSet,
                                       Set<String> phoneNumberDbSet,
                                       Set<String> emailDbSet) {
        Set<String> collectUsername = new HashSet<>();
        Set<String> collectPhoneNumber = new HashSet<>();
        Set<String> collectEmail = new HashSet<>();

        sysUserVOS.forEach(sysUserVO -> {
            collectUsername.add(sysUserVO.getUsername());
            collectPhoneNumber.add(sysUserVO.getPhoneNumber());
            collectEmail.add(sysUserVO.getEmail());
        });

        if (collectUsername.isEmpty()) {
            throw new ServiceException("当前excel中用户名为空，请检查数据");
        }


        usernameDbSet.addAll(sysUserMapper.queryUsername(collectUsername));
        if (!collectPhoneNumber.isEmpty()) {
            phoneNumberDbSet.addAll(sysUserMapper.queryPhoneNumber(collectPhoneNumber));
        }
        if (!collectEmail.isEmpty()) {
            emailDbSet.addAll(sysUserMapper.queryEmail(collectEmail));
        }
    }

    /**
     * 过滤重复数据
     */
    private boolean filterRepeatData(SysUserVO sysUserVO,
                                     List<SysUserVO> errorUserVos,
                                     Set<String> usernameRepeatSet,
                                     Set<String> phoneNumberRepeatSet,
                                     Set<String> emailRepeatSet) {
        if (!usernameRepeatSet.isEmpty()) {
            if (usernameRepeatSet.contains(sysUserVO.getUsername())) {
                sysUserVO.setImportErrorMsg("当前excel附件中用户名：" + sysUserVO.getUsername() + " 重复，请检查");
                errorUserVos.add(sysUserVO);
                return false;
            }
        }

        if (!phoneNumberRepeatSet.isEmpty()) {
            sysUserVO.setImportErrorMsg("当前excel附件中电话号码：" + sysUserVO.getPhoneNumber() + " 重复，请检查");
            errorUserVos.add(sysUserVO);
            return false;
        }

        if (!emailRepeatSet.isEmpty()) {
            sysUserVO.setImportErrorMsg("当前excel附件中邮箱：" + sysUserVO.getEmail() + " 重复，请检查");
            errorUserVos.add(sysUserVO);
            return false;
        }

        return true;
    }

    /**
     * 过滤掉不合法的用户名
     */
    private boolean filterUsername(SysUserVO sysUserVO,
                                   List<SysUserVO> errorUserVos,
                                   Set<String> usernames) {
        if (!StringUtils.hasText(sysUserVO.getUsername())) {
            sysUserVO.setImportErrorMsg("请填写用户名");
            errorUserVos.add(sysUserVO);
            return false;
        }

        if (usernames.contains(sysUserVO.getUsername())) {
            sysUserVO.setImportErrorMsg("用户名已存在");
            errorUserVos.add(sysUserVO);
            return false;
        }
        return true;
    }

    /**
     * 过滤掉不合法的手机号码
     */
    private boolean filterPhoneNumber(SysUserVO sysUserVO, List<SysUserVO> errorUserVos, Set<String> phoneNumberDbSet) {
        String phoneNumber = sysUserVO.getPhoneNumber();
        if (StringUtils.hasText(phoneNumber)) {
            if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
                sysUserVO.setImportErrorMsg("手机号码格式错误，请检查数据");
                errorUserVos.add(sysUserVO);
                return false;
            }

            if (phoneNumberDbSet.contains(sysUserVO.getPhoneNumber())) {
                sysUserVO.setImportErrorMsg("手机号码已存在");
                errorUserVos.add(sysUserVO);
                return false;
            }
        }
        return true;
    }

    /**
     * 过滤掉不合法的邮箱
     */
    private boolean filterEmail(SysUserVO sysUserVO, List<SysUserVO> errorUserVos, Set<String> emailDbSet) {
        String email = sysUserVO.getEmail();
        if (StringUtils.hasText(email)) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                sysUserVO.setImportErrorMsg("电子邮箱格式错误，请检查数据");
                errorUserVos.add(sysUserVO);
                return false;
            }

            if (emailDbSet.contains(sysUserVO.getEmail())) {
                sysUserVO.setImportErrorMsg("电子邮箱已存在");
                errorUserVos.add(sysUserVO);
                return false;
            }
        }
        return true;
    }

    /**
     * 过滤掉不合法的昵称
     */
    private boolean filterNickname(SysUserVO sysUserVO, List<SysUserVO> errorUserVos) {
        if (!StringUtils.hasText(sysUserVO.getNickname())) {
            sysUserVO.setImportErrorMsg("请填写用户昵称");
            errorUserVos.add(sysUserVO);
            return false;
        }
        return true;
    }

    /**
     * 过滤掉不合法的字典数据（性别、状态）
     */
    private boolean filterDictData(SysUserVO sysUserVO, List<SysUserVO> errorUserVos, List<SysDictDataVO> userGender, List<SysDictDataVO> sysStatus, String genderJoin, String statusJoin) {
        List<SysDictDataVO> gender = userGender.stream().filter(ug -> ug.getLabel().equals(sysUserVO.getGender())).toList();
        List<SysDictDataVO> status = sysStatus.stream().filter(ug -> ug.getLabel().equals(sysUserVO.getStatus())).toList();
        if (gender.isEmpty()) {
            sysUserVO.setImportErrorMsg("请填写用户性别或用户性别不合法，可输入项为：" + genderJoin);
            errorUserVos.add(sysUserVO);
            return false;
        }
        if (status.isEmpty()) {
            sysUserVO.setImportErrorMsg("请填写用户状态或用户状态不合法，可输入项为：" + statusJoin);
            errorUserVos.add(sysUserVO);
            return false;
        }
        sysUserVO.setGender(gender.get(0).getValue());
        sysUserVO.setStatus(status.get(0).getValue());
        return true;
    }

    /**
     * 过滤掉不合法的角色数据
     */
    private boolean filterRole(SysUserVO sysUserVO, List<SysUserVO> errorUserVos, List<String> allRoleNameList) {
        if (StringUtils.hasText(sysUserVO.getRoleName())) {
            if (sysUserVO.getRoleName().contains("超级管理员")) {
                sysUserVO.setImportErrorMsg("不允许导入超级管理员角色用户");
                errorUserVos.add(sysUserVO);
                return false;
            }

            String[] roleNames = sysUserVO.getRoleName().split("、");
            for (String roleName : roleNames) {
                if (!allRoleNameList.contains(roleName)) {
                    sysUserVO.setImportErrorMsg("角色 " + roleName + " 不存在，请检查数据或联系管理员");
                    errorUserVos.add(sysUserVO);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 过滤掉不合法的部门数据
     */
    private boolean filterDept(SysUserVO sysUserVO, List<SysUserVO> errorUserVos, List<String> allDeptNameList) {
        List<String> deptLabelList = sysUserVO.getDeptLabelList();
        // 允许部门为空
        if (deptLabelList == null) {
            return true;
        }
        for (String deptLabel : deptLabelList) {
            if (!allDeptNameList.contains(deptLabel)) {
                sysUserVO.setImportErrorMsg("部门 " + deptLabel + " 不存在，请检查数据或联系管理员");
                errorUserVos.add(sysUserVO);
                return false;
            }
        }
        return true;
    }

    /**
     * 过滤掉不合法的岗位
     */
    private boolean filterPost(SysUserVO sysUserVO, List<SysUserVO> errorUserVos, List<SysDeptVO> sysDeptList) {
        List<String> deptLabelList = sysUserVO.getDeptLabelList() == null ? new ArrayList<>() : sysUserVO.getDeptLabelList();
        List<String> postLabelList = sysUserVO.getPostLabelList() == null ? new ArrayList<>() : sysUserVO.getPostLabelList();
        // 正常情况下部门集合和岗位集合大小是相同的，当岗位集合数量 > 部门集合数量时，即数据有误
        if (postLabelList.size() > deptLabelList.size()) {
            sysUserVO.setImportErrorMsg("部门与岗位数量不匹配，请检查数据");
            errorUserVos.add(sysUserVO);
            return false;
        }

        for (int i = 0; i < deptLabelList.size(); i++) {
            String deptLabel = deptLabelList.get(i);
            String postLabelsStr = postLabelList.get(i);
            List<SysDeptVO> targetDeptList = sysDeptList.stream().filter(dept -> dept.getName().equals(deptLabel)).toList();
            SysDeptVO targetDept = targetDeptList.get(0);
            // 没有部门但是有对应岗位的情况
            if (targetDept == null && !StringUtils.hasText(postLabelsStr)) {
                sysUserVO.setImportErrorMsg("请填写岗位对应的部门");
                errorUserVos.add(sysUserVO);
                return false;
            }
            // 部门不为空检查对应岗位
            if (targetDept != null) {
                // 部门下岗位不为空，检查填写岗位是否存在
                List<SysPost> sysPostList = targetDept.getSysPostList();
                if (sysPostList != null && StringUtils.hasText(postLabelsStr)) {
                    String[] postArray = postLabelsStr.split("、");
                    List<String> postNameList = sysPostList.stream().map(SysPost::getName).toList();
                    for (String postName : postArray) {
                        if (!postNameList.contains(postName)) {
                            sysUserVO.setImportErrorMsg(targetDept.getName() + " 下无 " + postName + " 岗位，请检查数据");
                            errorUserVos.add(sysUserVO);
                            return false;
                        }
                    }
                } else {
                    // 部门下岗位为空，但导入数据下有岗位
                    if (StringUtils.hasText(postLabelsStr)) {
                        sysUserVO.setImportErrorMsg(targetDept.getName() + " 下无 " + postLabelsStr + " 岗位，请检查数据");
                        errorUserVos.add(sysUserVO);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // 批量保存
    private void batchInsert(List<SysUserVO> importUserVos, List<SysRole> roleList, List<SysDeptVO> sysDeptList) {
        List<SysUser> sysUserList = new ArrayList<>();
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        List<SysUserDept> sysUserDeptList = new ArrayList<>();
        List<SysUserPost> sysUserPostList = new ArrayList<>();

        // 构建用户、用户角色、用户部门、用户岗位 数据
        importUserVos.forEach(sysUserVO -> {
            SysUser sysUser = new SysUser();
            String userId = String.valueOf(IdWorker.getId(sysUser));
            BeanUtils.copyProperties(sysUserVO, sysUser);
            sysUser.setPassword(SecurityUtils.encryptPassword(sysSettingService.getDefaultPassword()));
            sysUser.setCreateTime(DateUtils.now());
            sysUser.setCreateId(LoginUserContext.getUserId());
            sysUser.setDelFlag("0");
            sysUser.setStatus("0");
            sysUser.setRegisterType("2");
            sysUser.setId(userId);

            // 构建用户
            sysUserList.add(sysUser);
            // 构建用户角色
            String roleName = sysUserVO.getRoleName();
            if (StringUtils.hasText(roleName)) {
                String[] roleNames = roleName.split("、");
                for (String name : roleNames) {
                    List<String> roleIds = roleList.stream().filter(role -> role.getName().equals(name)).map(SysRole::getId).toList();
                    if (!roleIds.isEmpty()) {
                        SysUserRole sysUserRole = new SysUserRole(userId, roleIds.get(0), DateUtils.now(), LoginUserContext.getUserId());
                        sysUserRoleList.add(sysUserRole);
                    }
                }
            }
            // 构建部门/岗位
            List<String> deptLabelList = sysUserVO.getDeptLabelList() == null ? new ArrayList<>() : sysUserVO.getDeptLabelList();
            List<String> postLabelList = sysUserVO.getPostLabelList() == null ? new ArrayList<>() : sysUserVO.getPostLabelList();
            if (!deptLabelList.isEmpty()) {
                // 循环计数器
                AtomicInteger index = new AtomicInteger();
                deptLabelList.forEach(deptLabel -> {
                    List<SysDeptVO> deptVOList = sysDeptList.stream().filter(dept -> dept.getName().equals(deptLabel)).toList();
                    if (!deptVOList.isEmpty()) {
                        SysDeptVO sysDeptVO = deptVOList.get(0);
                        // 构建部门
                        SysUserDept sysUserDept = new SysUserDept(userId, sysDeptVO.getId(), DateUtils.now(), LoginUserContext.getUserId(), "1");
                        sysUserDeptList.add(sysUserDept);

                        // 构建部门下岗位
                        List<SysPost> sysPostList = sysDeptVO.getSysPostList();
                        // deptLabelList 与 postLabelList 集合size 相同，通过循环 deptLabelList 获取index从 postLabelList 中获取对应元素
                        String postStrNames = postLabelList.get(index.get());
                        if (StringUtils.hasText(postStrNames)) {
                            String[] postNames = postStrNames.split("、");
                            for (String postName : postNames) {
                                List<String> postIds = sysPostList.stream().filter(post -> post.getName().equals(postName)).map(SysPost::getId).toList();
                                if (!postIds.isEmpty()) {
                                    SysUserPost sysUserPost = new SysUserPost(userId, postIds.get(0), DateUtils.now(), LoginUserContext.getUserId());
                                    sysUserPostList.add(sysUserPost);
                                }
                            }
                        }
                    }
                    index.getAndIncrement();
                });
            }
        });

        // 保存用户数据
        SysUserServiceImpl sysUserService = (SysUserServiceImpl) AopContext.currentProxy();
        sysUserService.saveBatch(sysUserList);
        // 保存用户角色数据
        sysUserRoleService.save(sysUserRoleList);
        // 保存用户部门数据
        sysUserDeptService.save(sysUserDeptList);
        // 保存用户岗位数据
        sysUserPostService.save(sysUserPostList);
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
        sysUser.setCreateId(LoginUserContext.getUserId());
        sysUser.setCreateTime(now);
        sysUser.setDelFlag("0");
        sysUser.setRegisterType("0");
        sysUser.setPasswordUpdateTime(now);
        sysUserMapper.insert(sysUser);
        return sysUser.getId();
    }

    // 更细用户信息
    private String update(SysUser sysUser) {
        sysUser.setUpdateId(LoginUserContext.getUserId());
        sysUser.setCreateTime(DateUtils.now());
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

}
