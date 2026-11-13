package com.lihua.strategy.saveuserregister;

import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.SysUserRole;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.service.SysRoleService;
import com.lihua.service.SysUserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SaveRoleStrategyImpl implements SaveRegisterUserAssociatedStrategy {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Override
    public void saveRegisterUserAssociated(String userId, SysSettingDTO.SignUpSetting signUpSetting) {
        // 用户角色关联表（过滤已删除的角色——删除操作不清理注册配置，JSON 可能残留悬空引用；空配置或全失效则不写入）
        List<String> validRoleIds = sysRoleService.queryExistIds(signUpSetting.getRoleIds());
        if (validRoleIds.isEmpty()) {
            return;
        }

        LocalDateTime now = DateUtils.now();
        List<SysUserRole> sysUserRoles = new ArrayList<>(validRoleIds.size());
        validRoleIds.forEach(roleId -> sysUserRoles.add(new SysUserRole(userId, roleId, now, null)));
        sysUserRoleService.save(sysUserRoles);
    }
}
