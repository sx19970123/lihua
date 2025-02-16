package com.lihua.strategy.system.authentication.impl.loginsetting;

import com.lihua.model.security.LoginUser;
import com.lihua.entity.system.SysSetting;
import com.lihua.model.system.dto.SysSettingDTO;
import com.lihua.service.system.setting.SysSettingService;
import com.lihua.strategy.system.authentication.CheckLoginSettingStrategy;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.json.JsonUtils;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 检查密码是否需要修改
 */
@Component
@Order(1)
public class CheckUpdatePasswordStrategyImpl implements CheckLoginSettingStrategy {

    @Resource
    private SysSettingService sysSettingService;

    final String COMPONENT_NAME = "LoginSettingResetPassword";

    @Override
    public String checkSetting(LoginUser loginUser) {

        // 用户密码与默认密码相同
        if (SecurityUtils.matchesPassword(sysSettingService.getDefaultPassword(), loginUser.getPassword())) {
            return COMPONENT_NAME;
        }

        // 获取定期修改密码配置
        SysSetting intervalUpdatePasswordSetting = sysSettingService.getSysSettingByComponentName("IntervalUpdatePasswordSetting");
        SysSettingDTO.IntervalUpdatePasswordSetting updatePasswordSetting = JsonUtils.toObject(intervalUpdatePasswordSetting.getSettingJson(), SysSettingDTO.IntervalUpdatePasswordSetting.class);

        boolean enable = updatePasswordSetting.isEnable();

        if (!enable) {
            return null;
        }

        // 更新周期
        Integer interval = updatePasswordSetting.getInterval();

        // 周期单位
        String unit = updatePasswordSetting.getUnit();

        // 上次更新密码时间
        LocalDateTime passwordUpdateTime = loginUser.getUser().getPasswordUpdateTime();

        LocalDateTime targetTime = null;
        switch (unit) {
            case "day": {
                targetTime = passwordUpdateTime.plusDays(interval);
                break;
            }
            case "week": {
                targetTime = passwordUpdateTime.plusWeeks(interval);
                break;
            }
            case "month": {
                targetTime = passwordUpdateTime.plusMonths(interval);
                break;
            }
            case "year": {
                targetTime = passwordUpdateTime.plusYears(interval);
                break;
            }
        }

        // 当前时间在目标时间之后，需要修改密码
        if (DateUtils.now().isAfter(targetTime)) {
            return COMPONENT_NAME;
        }

        return null;
    }
}
