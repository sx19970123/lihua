package com.lihua.system.strategy.impl.saveRegisterUserAssociated;

import com.lihua.system.entity.SysUserDept;
import com.lihua.system.model.dto.SysSettingDTO;
import com.lihua.system.service.SysUserDeptService;
import com.lihua.system.strategy.SaveRegisterUserAssociatedStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SaveDeptStrategyImpl implements SaveRegisterUserAssociatedStrategy {
    @Resource
    private SysUserDeptService sysUserDeptService;

    @Override
    public void saveRegisterUserAssociated(String userId, SysSettingDTO.SignInSetting signInSetting) {
        // 用户部门关联表
        List<String> deptIds = signInSetting.getDeptIds();
        if (!deptIds.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            String defaultDeptId = signInSetting.getDefaultDeptId();
            List<SysUserDept> sysUserDeptList  = new ArrayList<>(deptIds.size());
            deptIds.forEach(deptId -> sysUserDeptList.add(new SysUserDept(userId, deptId, now, null, deptId.equals(defaultDeptId) ? "0" : "1")));
            sysUserDeptService.save(sysUserDeptList);
        }

    }
}
