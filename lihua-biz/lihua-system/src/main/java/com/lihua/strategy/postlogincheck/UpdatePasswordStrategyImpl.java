package com.lihua.strategy.postlogincheck;

import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.SysUser;
import com.lihua.mapper.SysUserMapper;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.security.model.LoginUserSession;
import com.lihua.security.utils.SecurityUtils;
import com.lihua.service.SysSettingService;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 检查密码是否需要修改
 */
@Component
@Order(1)
public class UpdatePasswordStrategyImpl implements PostLoginCheckStrategy {

    @Resource
    private SysSettingService sysSettingService;

    @Resource
    private SysUserMapper sysUserMapper;

    final String COMPONENT_NAME = "UserSetupResetPassword";

    @Override
    public String check(LoginUserSession loginUserSession) {

        // 用户密码与默认密码相同（密码哈希自查 DB，会话不携带密码）
        SysUser user = sysUserMapper.selectById(loginUserSession.getUser().getId());
        if (user == null) {
            return null;
        }
        if (SecurityUtils.matchesPassword(sysSettingService.getDefaultPassword(), user.getPassword())) {
            return COMPONENT_NAME;
        }

        // 获取定期修改密码配置
        SysSettingDTO.IntervalUpdatePasswordSetting updatePasswordSetting = sysSettingService.getIntervalUpdatePasswordSetting();
        if (updatePasswordSetting == null) {
            return null;
        }

        boolean enable = updatePasswordSetting.isEnable();
        if (!enable) {
            return null;
        }

        // 更新周期
        Integer interval = updatePasswordSetting.getInterval();

        // 周期单位
        String unit = updatePasswordSetting.getUnit();

        // 上次更新密码时间
        LocalDateTime passwordUpdateTime = loginUserSession.getUser().getPasswordUpdateTime();

        // 配置或数据字段缺失时视为未启用检查（历史数据/手改库防御，避免 NPE 打断登录）
        if (interval == null || unit == null || passwordUpdateTime == null) {
            return null;
        }

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
            // 未知周期单位视为未配置
            default: {
                return null;
            }
        }

        // 当前时间在目标时间之后，需要修改密码
        if (DateUtils.now().isAfter(targetTime)) {
            return COMPONENT_NAME;
        }

        return null;
    }
}
