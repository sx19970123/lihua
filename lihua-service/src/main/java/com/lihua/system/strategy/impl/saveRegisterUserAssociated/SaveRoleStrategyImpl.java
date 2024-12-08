package com.lihua.system.strategy.impl.saveRegisterUserAssociated;

import com.lihua.system.entity.SysUserRole;
import com.lihua.system.model.dto.SysSettingDTO;
import com.lihua.system.service.SysUserRoleService;
import com.lihua.system.strategy.SaveRegisterUserAssociatedStrategy;
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
