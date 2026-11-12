package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.SysRole;
import com.lihua.entity.SysUser;
import com.lihua.mapper.SysRoleMapper;
import com.lihua.mapper.SysUserMapper;
import com.lihua.model.dto.SysRoleDTO;
import com.lihua.model.dto.SysRoleUserDTO;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.model.vo.SysRoleUserVO;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.security.utils.PermissionUpdateUtils;
import com.lihua.service.SysRoleService;
import com.lihua.service.SysSettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import com.lihua.common.enums.SysStatusEnum;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysSettingService sysSettingService;

    @Override
    public IPage<SysRole> queryPage(SysRoleDTO sysRoleDTO) {
        IPage<SysRole> iPage = new Page<>(sysRoleDTO.getPageNum(),sysRoleDTO.getPageSize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        // 角色名称
        if (StringUtils.hasText(sysRoleDTO.getName())) {
            queryWrapper.lambda().like(SysRole::getName,sysRoleDTO.getName());
        }
        // 角色编码
        if (StringUtils.hasText(sysRoleDTO.getCode())) {
            queryWrapper.lambda().like(SysRole::getCode,sysRoleDTO.getCode());
        }
        // 角色状态
        if (StringUtils.hasText(sysRoleDTO.getStatus())) {
            queryWrapper.lambda().eq(SysRole::getStatus,sysRoleDTO.getStatus());
        }
        // 按雪花算法id排序
        queryWrapper.lambda().orderByDesc(SysRole::getCreateTime);
        sysRoleMapper.selectPage(iPage,queryWrapper);

        return iPage;
    }

    @Override
    public SysRole queryById(String id) {
        return sysRoleMapper.queryById(id);
    }

    @Override
    public List<String> queryExistIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(SysRole::getId).in(SysRole::getId, ids);
        return sysRoleMapper.selectList(queryWrapper).stream().map(SysRole::getId).toList();
    }

    @Transactional
    @Override
    public String save(SysRole sysRole) {
        String id;
        // 唯一性数据校验
        checkRoleCode(sysRole);
        checkRoleName(sysRole);
        // 保存role表数据
        if (StringUtils.hasText(sysRole.getId())) {
            id = update(sysRole);
        } else {
            id = insert(sysRole);
        }
        // 保存关联表数据
        saveRoleMenu(id, sysRole.getMenuIds());
        return id;
    }

    private String insert(SysRole sysRole) {
        sysRoleMapper.insert(sysRole);
        return sysRole.getId();
    }

    private String update(SysRole sysRole) {
        sysRoleMapper.updateById(sysRole);
        return sysRole.getId();
    }

    private void saveRoleMenu(String roleId,List<String> menuIds) {
        sysRoleMapper.deleteRoleMenuByRoleId(roleId);
        if (!menuIds.isEmpty()) {
            sysRoleMapper.insertRoleMenu(roleId,menuIds);
        }
        // 菜单权限已变：置红点标记 + WS 定向在线提示（会话保留，reloadData 重建后新权限生效）
        PermissionUpdateUtils.markChanged(sysRoleMapper.selectUserIdsByRoleId(roleId));
    }

    private void checkRoleCode(SysRole sysRole) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRole::getCode,sysRole.getCode());

        List<SysRole> sysRoles = sysRoleMapper.selectList(queryWrapper);

        if (sysRoles.isEmpty()) {
            return;
        }
        if (sysRoles.size() > 1) {
            throw new ServiceException("角色编码已存在");
        }
        if (!sysRoles.get(0).getId().equals(sysRole.getId())) {
            throw new ServiceException("角色编码已存在");
        }
    }

    private void checkRoleName(SysRole sysRole) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRole::getName,sysRole.getName());

        List<SysRole> sysRoles = sysRoleMapper.selectList(queryWrapper);
        if (sysRoles.isEmpty()) {
            return;
        }
        if (sysRoles.size() > 1) {
            throw new ServiceException("角色名称已存在");
        }
        if (!sysRoles.get(0).getId().equals(sysRole.getId())) {
            throw new ServiceException("角色名称已存在");
        }
    }

    @Override
    public void deleteByIds(List<String> ids) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(SysRole::getId,ids)
                .eq(SysRole::getStatus, SysStatusEnum.NORMAL.getValue());
        Long count = sysRoleMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("角色状态为正常，不允许删除");
        }

        Long menuCount = sysRoleMapper.selectRoleMenuCountByRoleIds(ids);
        Long userCount = sysRoleMapper.selectUserRoleCountByRoleIds(ids);
        checkSignUpSetting(ids);
        if (menuCount == 0 && userCount == 0) {
            sysRoleMapper.deleteByIds(ids);
        } else {
            throw new ServiceException("角色已绑定菜单/用户，不允许删除");
        }
    }

    private void checkSignUpSetting(List<String> ids) {
        SysSettingDTO.SignUpSetting signUpSetting = sysSettingService.getSignUpSetting();
        if (signUpSetting == null) {
            return;
        }

        List<String> roleIds = signUpSetting.getRoleIds();
        if (roleIds != null && ids.stream().anyMatch(roleIds::contains)) {
            throw new ServiceException("角色已被自助注册配置引用，请先在「自助注册设置」中移除");
        }
    }

    @Override
    public String updateStatus(String id, String currentStatus) {
        checkRoleExists(id);
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        String status = SysStatusEnum.toggle(currentStatus);

        updateWrapper.lambda()
                .set(SysRole::getStatus, status)
                .set(SysRole::getUpdateId, LoginUserContext.getUserId())
                .set(SysRole::getUpdateTime, DateUtils.now())
                .eq(SysRole::getId, id);
        sysRoleMapper.update(null, updateWrapper);
        return status;
    }

    @Override
    public IPage<SysRoleUserVO> queryUserPage(String roleId, SysRoleUserDTO sysRoleUserDTO) {
        checkRoleExists(roleId);
        return sysRoleMapper.selectUserPageByRoleId(
                new Page<>(sysRoleUserDTO.getPageNum(), sysRoleUserDTO.getPageSize()), roleId, sysRoleUserDTO);
    }

    @Override
    public void saveUsers(String roleId, List<String> userIds) {
        checkRoleExists(roleId);
        List<String> distinctIds = userIds.stream().distinct().toList();
        // 校验用户存在性，防止无效id写入脏关联
        Long userCount = sysUserMapper.selectCount(new QueryWrapper<SysUser>().in("id", distinctIds));
        if (userCount != distinctIds.size()) {
            throw new ServiceException("包含无效用户");
        }
        // 已授权用户静默跳过，保证幂等
        List<String> authorizedIds = sysRoleMapper.selectUserIdsByRoleIdAndUserIds(roleId, distinctIds);
        List<String> newUserIds = distinctIds.stream().filter(id -> !authorizedIds.contains(id)).toList();
        if (!newUserIds.isEmpty()) {
            sysRoleMapper.insertUserRole(roleId, newUserIds);
            // 置红点标记 + WS 定向在线提示
            PermissionUpdateUtils.markChanged(newUserIds);
        }
    }

    @Override
    public void deleteUsers(String roleId, List<String> userIds) {
        checkRoleExists(roleId);
        List<String> distinctUserIds = userIds.stream().distinct().toList();
        sysRoleMapper.deleteUserRoleByRoleIdAndUserIds(roleId, distinctUserIds);
        // 置红点标记 + WS 定向在线提示
        PermissionUpdateUtils.markChanged(distinctUserIds);
    }

    private void checkRoleExists(String id) {
        if (sysRoleMapper.selectById(id) == null) {
            throw new ServiceException("角色不存在");
        }
    }
}
