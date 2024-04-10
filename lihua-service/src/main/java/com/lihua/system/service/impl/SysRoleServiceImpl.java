package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.exception.ServiceException;
import com.lihua.system.entity.SysRole;
import com.lihua.system.mapper.SysRoleMapper;
import com.lihua.system.model.SysRoleDTO;
import com.lihua.system.service.SysMenuService;
import com.lihua.system.service.SysRoleService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public IPage<SysRole> findPage(SysRoleDTO sysRoleDTO) {
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
        queryWrapper.lambda().orderByDesc(SysRole::getId);
        sysRoleMapper.selectPage(iPage,queryWrapper);

        return iPage;
    }

    @Override
    public SysRole findById(String id) {
        return sysRoleMapper.findById(id);
    }

    @Transactional
    @Override
    public String save(SysRole sysRole) {
        String id;
        // 保存role表数据
        if (StringUtils.hasText(sysRole.getId())) {
            id = insert(sysRole);
        } else {
            id = update(sysRole);
        }
        // 保存关联表数据
        saveRoleMenu(id, sysRole.getMenuIds());
        return id;
    }

    private String insert(SysRole sysRole) {
        sysRole.setCreateId(LoginUserContext.getUserId());
        sysRole.setCreateTime(LocalDateTime.now());
        sysRoleMapper.insert(sysRole);
        return sysRole.getId();
    }

    private String update(SysRole sysRole) {
        sysRole.setUpdateId(LoginUserContext.getUserId());
        sysRole.setUpdateTime(LocalDateTime.now());
        sysRoleMapper.updateById(sysRole);
        return sysRole.getId();
    }

    private void saveRoleMenu(String roleId,List<String> menuIds) {
        sysRoleMapper.deleteRoleMenuByRoleId(roleId);
        sysRoleMapper.insertRoleMenu(roleId,menuIds);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        Long menuCount = sysRoleMapper.selectRoleMenuCount("role_id",ids);
        Long userCount = sysRoleMapper.selectUserRoleCount("role_id",ids);
        if (menuCount == 0 && userCount == 0) {
            sysRoleMapper.deleteBatchIds(ids);
        } else {
            throw new ServiceException("角色已绑定菜单/用户，不允许删除");
        }
    }
}
