package com.lihua.strategy.saveuserregister;

import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.SysUserDept;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.service.SysDeptService;
import com.lihua.service.SysUserDeptService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SaveDeptStrategyImpl implements SaveRegisterUserAssociatedStrategy {

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysUserDeptService sysUserDeptService;

    @Override
    public void saveRegisterUserAssociated(String userId, SysSettingDTO.SignUpSetting signUpSetting) {
        // 用户部门关联表（过滤已删除的部门——删除操作不清理注册配置，JSON 可能残留悬空引用；空配置或全失效则不写入——默认部门不在有效集合时无行命中默认标记，「有部门无默认部门」由登录后检查（DefaultDeptStrategyImpl）走「选择默认部门」向导步兜底）
        List<String> validDeptIds = sysDeptService.queryExistIds(signUpSetting.getDeptIds());
        if (validDeptIds.isEmpty()) {
            return;
        }

        LocalDateTime now = DateUtils.now();
        String defaultDeptId = signUpSetting.getDefaultDeptId();
        List<SysUserDept> sysUserDeptList = new ArrayList<>(validDeptIds.size());
        validDeptIds.forEach(deptId -> sysUserDeptList.add(new SysUserDept(userId, deptId, now, null, deptId.equals(defaultDeptId) ? "0" : "1")));
        sysUserDeptService.save(sysUserDeptList);
    }
}
