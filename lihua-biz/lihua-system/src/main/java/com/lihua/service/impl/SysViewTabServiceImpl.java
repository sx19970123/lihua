package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.common.exception.ServiceException;
import com.lihua.entity.SysViewTab;
import com.lihua.mapper.SysViewTabMapper;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.security.manager.LoginUserManager;
import com.lihua.security.model.CurrentRouter;
import com.lihua.security.model.CurrentViewTab;
import com.lihua.security.model.LoginUserSession;
import com.lihua.enums.MenuTypeEnum;
import com.lihua.enums.ViewTabFlagEnum;
import com.lihua.service.SysViewTabService;
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
                    .filter(routerVO -> MenuTypeEnum.PAGE.getValue().equals(routerVO.getType()) || MenuTypeEnum.LINK.getValue().equals(routerVO.getType()))
                    .map(CurrentRouter::getId)
                    .collect(Collectors.toSet());
        }

        // 获取收藏/固定的页面
        if (!menuIds.isEmpty()) {
            QueryWrapper<SysViewTab> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(SysViewTab::getUserId,userId)
                    .in(SysViewTab::getMenuId, menuIds)
                    .and(wrapper -> wrapper.eq(SysViewTab::getAffix, ViewTabFlagEnum.YES.getValue())
                            .or()
                            .eq(SysViewTab::getStar, ViewTabFlagEnum.YES.getValue()));
            sysUserStarViews = sysUserStarViewMapper.selectList(queryWrapper);
        }

        // 数据组合
        List<CurrentViewTab> viewVOS = new ArrayList<>();
        for (CurrentRouter route : routerVOList) {
            if (MenuTypeEnum.PAGE.getValue().equals(route.getType()) || MenuTypeEnum.LINK.getValue().equals(route.getType())) {
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
                                .setStar(ViewTabFlagEnum.YES.getValue().equals(star.getStar()))
                                .setAffix(ViewTabFlagEnum.YES.getValue().equals(star.getAffix()));
                    }
                });
                viewVOS.add(sysViewTabVO);
            }
        }
        return viewVOS;
    }

    @Override
    public CurrentViewTab save(SysViewTab sysStarView) {
        boolean hasAffix = StringUtils.hasText(sysStarView.getAffix());
        boolean hasStar = StringUtils.hasText(sysStarView.getStar());

        if (!hasAffix && !hasStar) {
            throw new ServiceException("参数错误");
        }

        UpdateWrapper<SysViewTab> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(SysViewTab::getUserId, LoginUserContext.getUserId())
                .eq(SysViewTab::getMenuId,sysStarView.getMenuId());
        if (hasAffix) {
            updateWrapper.lambda().set(SysViewTab::getAffix,sysStarView.getAffix());
        }
        if (hasStar) {
            updateWrapper.lambda().set(SysViewTab::getStar,sysStarView.getStar());
        }

        // 尝试更新数据，更新结果为0表示无数据，再执行插入
        int update = sysUserStarViewMapper.update(updateWrapper);

        if (update == 0) {
            sysStarView.setUserId(LoginUserContext.getUserId());
            sysUserStarViewMapper.insert(sysStarView);
        }

        // 会话回写与库内更新同语义：仅回传的字段写会话，未传字段保留会话现值
        // （单字段请求时另一侧不被抹成 false，避免会话与 DB 分叉）
        CurrentViewTab starView = null;
        LoginUserSession loginUserSession = LoginUserContext.getLoginUser();
        for (CurrentViewTab starViewVO : loginUserSession.getViewTabList()) {
            if (starViewVO.getMenuId().equals(sysStarView.getMenuId())) {
                if (hasAffix) {
                    starViewVO.setAffix(ViewTabFlagEnum.YES.getValue().equals(sysStarView.getAffix()));
                }
                if (hasStar) {
                    starViewVO.setStar(ViewTabFlagEnum.YES.getValue().equals(sysStarView.getStar()));
                }
                starView = starViewVO;
            }
        }
        // 更新LoginUser缓存
        LoginUserManager.setLoginUserCache(loginUserSession);
        return starView;
    }
}
