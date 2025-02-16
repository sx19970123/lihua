package com.lihua.service.system.role;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.exception.ServiceException;
import com.lihua.entity.system.SysRole;
import com.lihua.mapper.system.SysRoleMapper;
import com.lihua.model.system.dto.SysRoleDTO;
import com.lihua.service.system.menu.SysMenuService;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysMenuService sysMenuService;

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
        sysRole.setCreateId(LoginUserContext.getUserId());
        sysRole.setCreateTime(DateUtils.now());
        sysRole.setDelFlag("0");
        sysRoleMapper.insert(sysRole);
        return sysRole.getId();
    }

    private String update(SysRole sysRole) {
        sysRole.setUpdateId(LoginUserContext.getUserId());
        sysRole.setUpdateTime(DateUtils.now());
        sysRoleMapper.updateById(sysRole);
        return sysRole.getId();
    }

    private void saveRoleMenu(String roleId,List<String> menuIds) {
        sysRoleMapper.deleteRoleMenuByRoleId(roleId);
        if (!menuIds.isEmpty()) {
            sysRoleMapper.insertRoleMenu(roleId,menuIds);
        }
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
                .eq(SysRole::getStatus,"0");
        Long count = sysRoleMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("角色状态为正常，不允许删除");
        }

        Long menuCount = sysRoleMapper.selectRoleMenuCount("role_id",ids);
        Long userCount = sysRoleMapper.selectUserRoleCount("role_id",ids);
        if (menuCount == 0 && userCount == 0) {
            sysRoleMapper.deleteByIds(ids);
        } else {
            throw new ServiceException("角色已绑定菜单/用户，不允许删除");
        }
    }

    @Override
    public String updateStatus(String id, String currentStatus) {
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        String status = "0".equals(currentStatus) ? "1" : "0";

        updateWrapper.lambda()
                .set(SysRole::getStatus, status)
                .set(SysRole::getUpdateId, LoginUserContext.getUserId())
                .set(SysRole::getUpdateTime, DateUtils.now())
                .eq(SysRole::getId, id);
        sysRoleMapper.update(null, updateWrapper);
        return status;
    }
}
