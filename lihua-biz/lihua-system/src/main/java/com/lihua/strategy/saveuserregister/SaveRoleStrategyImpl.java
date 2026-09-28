package com.lihua.strategy.saveuserregister;

import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.SysUserRole;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.service.SysUserRoleService;
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
    public void saveRegisterUserAssociated(String userId, SysSettingDTO.SignUpSetting signUpSetting) {
        List<String> roleIds = signUpSetting.getRoleIds();
        // 用户角色关联表
        if (!roleIds.isEmpty()) {
            LocalDateTime now = DateUtils.now();
            List<SysUserRole> sysUserRoles = new ArrayList<>(roleIds.size());
            roleIds.forEach(roleId -> sysUserRoles.add(new SysUserRole(userId, roleId, now, null)));
            sysUserRoleService.save(sysUserRoles);
        }
    }
}
