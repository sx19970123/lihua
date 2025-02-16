package com.lihua.service.system.viewtab;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.model.security.CurrentViewTab;
import com.lihua.model.security.LoginUser;
import com.lihua.model.security.CurrentRouter;
import com.lihua.entity.system.SysViewTab;
import com.lihua.mapper.system.SysViewTabMapper;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.LoginUserManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysViewTabServiceImpl implements SysViewTabService {

    @Resource
    private SysViewTabMapper sysUserStarViewMapper;

    @Override
    public List<CurrentViewTab> selectByUserId(String userId, List<CurrentRouter> routerVOList) {
        // 部门id
        Set<String> menuIds = new HashSet<>();
        // 收藏/固定页面
        List<SysViewTab> sysUserStarViews = new ArrayList<>();

        // 获取页面id
        if (!routerVOList.isEmpty()) {
            menuIds = routerVOList
                    .stream()
                    .filter(routerVO -> "page".equals(routerVO.getType()) || "link".equals(routerVO.getType()))
                    .map(CurrentRouter::getId)
                    .collect(Collectors.toSet());
        }

        // 获取收藏/固定的页面
        if (!menuIds.isEmpty()) {
            QueryWrapper<SysViewTab> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(SysViewTab::getUserId,userId)
                    .in(SysViewTab::getMenuId, menuIds)
                    .and(wrapper -> wrapper.eq(SysViewTab::getAffix,"1")
                            .or()
                            .eq(SysViewTab::getStar,"1"));
            sysUserStarViews = sysUserStarViewMapper.selectList(queryWrapper);
        }

        // 数据组合
        List<CurrentViewTab> viewVOS = new ArrayList<>();
        for (CurrentRouter route : routerVOList) {
            if ("page".equals(route.getType()) || "link".equals(route.getType())) {
                CurrentViewTab sysViewTabVO = new CurrentViewTab();
                sysViewTabVO
                        .setLabel(route.getMeta().getLabel())
                        .setIcon(route.getMeta().getIcon())
                        .setRouterPathKey(route.getKey())
                        .setQuery(route.getQuery())
                        .setMenuId(route.getId())
                        .setMenuType(route.getType())
                        .setLinkOpenType(route.getMeta().getLinkOpenType())
                        .setLink(route.getMeta().getLink());
                // 判断是否进行收藏/固定
                sysUserStarViews.forEach(star -> {
                    if (star.getMenuId().equals(route.getId())) {
                        sysViewTabVO
                                .setStar("1".equals(star.getStar()))
                                .setAffix("1".equals(star.getAffix()));
                    }
                });
                viewVOS.add(sysViewTabVO);
            }
        }
        return viewVOS;
    }

    @Override
    public CurrentViewTab save(SysViewTab sysStarView) {
        UpdateWrapper<SysViewTab> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(SysViewTab::getUserId, LoginUserContext.getUserId())
                .eq(SysViewTab::getMenuId,sysStarView.getMenuId());
        boolean flag = true;

        if (StringUtils.hasText(sysStarView.getAffix())) {
            flag = false;
            updateWrapper.lambda().set(SysViewTab::getAffix,sysStarView.getAffix());
        }

        if (StringUtils.hasText(sysStarView.getStar())) {
            flag = false;
            updateWrapper.lambda().set(SysViewTab::getStar,sysStarView.getStar());
        }

        if (flag) {
            throw new RuntimeException("参数错误");
        }

        // 尝试更新数据，更新结果为0表示无数据，再执行插入
        int update = sysUserStarViewMapper.update(updateWrapper);

        if (update == 0) {
            sysStarView.setUserId(LoginUserContext.getUserId());
            sysUserStarViewMapper.insert(sysStarView);
        }

        // 重新设置loginUserContext
        CurrentViewTab starView = null;
        LoginUser loginUser = LoginUserContext.getLoginUser();
        for (CurrentViewTab starViewVO : loginUser.getViewTabList()) {
            if (starViewVO.getMenuId().equals(sysStarView.getMenuId())) {
                starViewVO.setAffix("1".equals(sysStarView.getAffix()))
                        .setStar("1".equals(sysStarView.getStar()));
                starView = starViewVO;
            }
        }
        // 更新LoginUser缓存
        LoginUserManager.setLoginUserCache(loginUser);
        return starView;
    }
}
