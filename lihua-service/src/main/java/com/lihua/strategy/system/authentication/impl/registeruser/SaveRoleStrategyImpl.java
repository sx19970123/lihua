package com.lihua.strategy.system.authentication.impl.registeruser;

import com.lihua.entity.system.SysUserRole;
import com.lihua.model.system.dto.SysSettingDTO;
import com.lihua.service.system.user.SysUserRoleService;
import com.lihua.strategy.system.authentication.SaveRegisterUserAssociatedStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SaveRoleStrategyImpl implements SaveRegisterUserAssociatedStrategy {

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Override
    public void saveRegisterUserAssociated(String userId, SysSettingDTO.SignInSetting signInSetting) {
        List<String> roleIds = signInSetting.getRoleIds();
        // 用户角色关联表
        if (!roleIds.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            List<SysUserRole> sysUserRoles = new ArrayList<>(roleIds.size());
            roleIds.forEach(roleId -> sysUserRoles.add(new SysUserRole(userId, roleId, now, null)));
            sysUserRoleService.save(sysUserRoles);
        }
    }
}
