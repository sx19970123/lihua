package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.exception.ServiceException;
import com.lihua.model.dict.SysDictDataVO;
import com.lihua.model.excel.ExcelImportResult;
import com.lihua.system.entity.*;
import com.lihua.system.mapper.SysDeptMapper;
import com.lihua.system.mapper.SysRoleMapper;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.system.model.dto.SysUserDTO;
import com.lihua.system.model.dto.SysUserDeptDTO;
import com.lihua.system.model.vo.SysDeptVO;
import com.lihua.system.model.vo.SysUserVO;
import com.lihua.system.service.*;
import com.lihua.utils.dict.DictUtils;
import com.lihua.utils.excel.ExcelUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
        // 无法倒入的用户列表
        List<SysUserVO> errorUserVos = new ArrayList<>();
        // 可倒入的用户列表
        List<SysUserVO> importUserVos = new ArrayList<>();

        // 1. 验证用户名/用户昵称是否存在/合法
        List<String> usernameList = sysUserVOS.stream().map(SysUserVO::getUsername).toList();
        List<String> usernames = sysUserMapper.findUsername(usernameList);
        for (SysUserVO userVO : sysUserVOS) {
            if (usernames.contains(userVO.getUsername())) {
                userVO.setImportErrorMsg("用户名已存在");
                errorUserVos.add(userVO);
            } else if (!StringUtils.hasText(userVO.getUsername())) {
                userVO.setImportErrorMsg("请填写用户名");
                errorUserVos.add(userVO);
            } else if (!StringUtils.hasText(userVO.getNickname())) {
                userVO.setImportErrorMsg("请填写用户昵称");
                errorUserVos.add(userVO);
            } else {
                importUserVos.add(userVO);
            }
        }

        List<SysDictDataVO> sysStatus = DictUtils.getDictData("sys_status");
        List<SysDictDataVO> userGender = DictUtils.getDictData("user_gender");
        String genderJoin = userGender.stream().map(SysDictDataVO::getLabel).collect(Collectors.joining("、"));
        String statusJoin = sysStatus.stream().map(SysDictDataVO::getLabel).collect(Collectors.joining("、"));
        // 2. 验证涉及到的数据字典相关，并将label转换为value
        importUserVos = importUserVos.stream().filter(sysUserVO -> {
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
        }).toList();

        // 3. 获取全部role name，去数据库中查询，查询出数据库存在的 role 数据
        Set<String> roleNameSet = new HashSet<>();
        importUserVos.forEach(sysUserVO -> {
            if (StringUtils.hasText(sysUserVO.getRoleName())) {
                String[] roleNames = sysUserVO.getRoleName().split("、");
                roleNameSet.addAll(Arrays.asList(roleNames));
            }
        });

        List<SysRole> roleList = new ArrayList<>();
        if (!roleNameSet.isEmpty()) {
            // 获取数据库中所有相关角色信息
            roleList = sysRoleMapper.findByRoleNames(roleNameSet);
            List<String> allRoleNameList = roleList.stream().map(SysRole::getName).toList();
            // 过滤匹配到数据库中存在角色名称的数据（角色为空的数据不进行过滤）
            importUserVos = importUserVos.stream().filter(sysUserVO -> {
                if (StringUtils.hasText(sysUserVO.getRoleName())) {
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
            }).toList();
        }

        // 4. 获取全部dept name，去数据库中查询，查询出数据库存在的 dept 数据
        Set<String> deptNameSet = new HashSet<>();
        importUserVos.forEach(sysUserVO -> {
            List<String> deptLabelList = sysUserVO.getDeptLabelList();
            if (!deptLabelList.isEmpty()) {
                deptNameSet.addAll(deptLabelList);
            }
        });

        List<SysDeptVO> sysDepts;
        if (!deptNameSet.isEmpty()) {
            // 获取数据库中所有相关部门（连同部门下岗位）信息
            sysDepts = sysDeptMapper.findByDeptNames(deptNameSet);
            List<String> allDeptNameList = sysDepts.stream().map(SysDept::getName).toList();
            // 过滤匹配到数据库中存在部门名称的数据（部门为空的数据不进行过滤）
            importUserVos = importUserVos.stream().filter(sysUserVO -> {
                List<String> deptLabelList = sysUserVO.getDeptLabelList();
                for (String deptLabel : deptLabelList) {
                    if (!allDeptNameList.contains(deptLabel)) {
                        sysUserVO.setImportErrorMsg("部门 " + deptLabel + " 不存在，请检查数据或联系管理员");
                        errorUserVos.add(sysUserVO);
                        return false;
                    }
                }
                return true;
            }).toList();
        } else {
            sysDepts = new ArrayList<>();
        }

        // 5. 匹配岗位数据
        importUserVos = importUserVos.stream().filter(sysUserVO -> {
            List<String> deptLabelList = sysUserVO.getDeptLabelList();
            List<String> postLabelList = sysUserVO.getPostLabelList();
            // 正常情况下部门集合和岗位集合大小是相同的，当岗位集合数量 > 部门集合数量时，即数据有误
            if (postLabelList.size() > deptLabelList.size()) {
                sysUserVO.setImportErrorMsg("部门与岗位数量不匹配，请检查数据");
                errorUserVos.add(sysUserVO);
                return false;
            }

            for (int i = 0; i < deptLabelList.size(); i++) {
                String deptLabel = deptLabelList.get(i);
                String postLabelsStr = postLabelList.get(i);
                List<SysDeptVO> targetDeptList = sysDepts.stream().filter(dept -> dept.getName().equals(deptLabel)).toList();
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
        }).toList();

        // 6. 处理完毕后获得两批数据：通过校验可导入 / 数据存在异常需用户重新处理
        // 导出错误数据集
        String errExcelPath = null;
        if (!errorUserVos.isEmpty()) {
            String errExcelName = LoginUserContext.getUserId() + "_导入失败_" + UUID.randomUUID().toString().replace("-","");
            errExcelPath = ExcelUtils.excelExport(errorUserVos, SysUserVO.class, errExcelName);
        }
        // 插入数据
        if (!importUserVos.isEmpty()) {
            batchInsert(importUserVos, roleList, sysDepts);
        }

        // 返回汇总的导入结果
        return new ExcelImportResult((sysUserVOS.size() == importUserVos.size()) && sysUserVOS.isEmpty(),
                sysUserVOS.size(),
                importUserVos.size(),
                errorUserVos.size(),
                errExcelPath);
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
            // todo 暂时写死默认密码，后期统一管理
            sysUser.setPassword(SecurityUtils.encryptPassword("123456"));
            sysUser.setCreateTime(LocalDateTime.now());
            sysUser.setCreateId(LoginUserContext.getUserId());
            sysUser.setDelFlag("0");
            sysUser.setStatus("0");
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
                        SysUserRole sysUserRole = new SysUserRole(userId, roleIds.get(0), LocalDateTime.now(), LoginUserContext.getUserId());
                        sysUserRoleList.add(sysUserRole);
                    }
                }
            }
            // 构建部门/岗位
            List<String> deptLabelList = sysUserVO.getDeptLabelList();
            List<String> postLabelList = sysUserVO.getPostLabelList();
            if (!deptLabelList.isEmpty()) {
                // 循环计数器
                AtomicInteger index = new AtomicInteger();
                deptLabelList.forEach(deptLabel -> {
                    List<SysDeptVO> deptVOList = sysDeptList.stream().filter(dept -> dept.getName().equals(deptLabel)).toList();
                    if (!deptVOList.isEmpty()) {
                        SysDeptVO sysDeptVO = deptVOList.get(0);
                        // 构建部门
                        SysUserDept sysUserDept = new SysUserDept(userId, sysDeptVO.getId(), LocalDateTime.now(), LoginUserContext.getUserId(), "1");
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
                                    SysUserPost sysUserPost = new SysUserPost(userId, postIds.get(0), LocalDateTime.now(), LoginUserContext.getUserId());
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
        saveBatch(sysUserList);
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
