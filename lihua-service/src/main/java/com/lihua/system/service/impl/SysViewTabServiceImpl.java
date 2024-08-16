package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.CurrentViewTab;
import com.lihua.model.security.LoginUser;
import com.lihua.model.security.CurrentRouter;
import com.lihua.system.entity.SysViewTab;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.mapper.SysViewTabMapper;
import com.lihua.system.service.SysViewTabService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.LoginUserManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysViewTabServiceImpl implements SysViewTabService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private SysViewTabMapper sysUserStarViewMapper;

    @Override
    public List<CurrentViewTab> selectByUserId(String userId, List<CurrentRouter> routerVOList) {

        if (routerVOList.isEmpty()) {
            throw new ServiceException("请联系管理员分配角色&菜单");
        }

        // 获取收藏菜单数据
        QueryWrapper<SysViewTab> queryWrapper = new QueryWrapper<>();
        Set<String> menuIds = routerVOList
                .stream()
                .filter(routerVO -> "page".equals(routerVO.getType()) || "link".equals(routerVO.getType()))
                .map(CurrentRouter::getId)
                .collect(Collectors.toSet());

        if (menuIds.isEmpty()) {
            throw new ServiceException("请联系管理员为角色分配页面");
        }

        queryWrapper.lambda()
                .eq(SysViewTab::getUserId,userId)
                .in(SysViewTab::getMenuId, menuIds)
                .and(wrapper -> wrapper.eq(SysViewTab::getAffix,"1")
                        .or()
                        .eq(SysViewTab::getStar,"1"));

        List<SysViewTab> sysUserStarViews = sysUserStarViewMapper.selectList(queryWrapper);

        // 数据组合
        List<CurrentViewTab> viewVOS = new ArrayList<>();
        routerVOList
                .stream()
                .filter(route -> "page".equals(route.getType()) || "link".equals(route.getType()))
                .forEach(route -> {
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
                });

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
