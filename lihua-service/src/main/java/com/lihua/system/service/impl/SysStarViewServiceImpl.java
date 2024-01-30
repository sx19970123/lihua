package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.model.security.RouterVO;
import com.lihua.model.security.SysStarViewVO;
import com.lihua.system.entity.SysStarView;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.mapper.SysStarViewMapper;
import com.lihua.system.service.SysStarViewService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.LoginUserReset;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysStarViewServiceImpl implements SysStarViewService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private SysStarViewMapper sysUserStarViewMapper;

    @Override
    public List<SysStarViewVO> selectByUserId(String userId) {
        // 获取当前用户菜单数据
        List<RouterVO> routerVOList = sysMenuMapper.selectPermsByUserId(userId);

        // 获取收藏菜单数据
        QueryWrapper<SysStarView> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysStarView::getUserId,userId)
                .in(SysStarView::getRouterPathKey,
                        routerVOList
                        .stream()
                        .filter(routerVO -> "page".equals(routerVO.getType()))
                        .map(RouterVO::getKey)
                        .collect(Collectors.toSet())
                ).and(wrapper -> wrapper.eq(SysStarView::getAffix,"1").or().eq(SysStarView::getStar,"1"));

        List<SysStarView> sysUserStarViews = sysUserStarViewMapper.selectList(queryWrapper);

        // 数据组合
        List<SysStarViewVO> viewVOS = new ArrayList<>();
        routerVOList
                .stream()
                .filter(route -> "page".equals(route.getType()))
                .forEach(route -> {
                    SysStarViewVO sysUserStarViewVO = new SysStarViewVO();
                    sysUserStarViewVO
                            .setLabel(route.getMeta().getLabel())
                            .setIcon(route.getMeta().getIcon())
                            .setRouterPathKey(route.getKey())
                            .setQuery(route.getQuery());
                    // 判断是否进行收藏/固定
                    sysUserStarViews.forEach(star -> {
                        if (star.getRouterPathKey().equals(route.getKey())) {
                            sysUserStarViewVO
                                    .setStar("1".equals(star.getStar()))
                                    .setAffix("1".equals(star.getAffix()));
                        }
                    });

                    viewVOS.add(sysUserStarViewVO);
                });

        return viewVOS;
    }

    @Override
    public SysStarViewVO save(SysStarView sysStarView) {
        UpdateWrapper<SysStarView> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(SysStarView::getUserId, LoginUserContext.getUserId())
                .eq(SysStarView::getRouterPathKey,sysStarView.getRouterPathKey());
        boolean flag = true;

        if (StringUtils.hasText(sysStarView.getAffix())) {
            flag = false;
            updateWrapper.lambda().set(SysStarView::getAffix,sysStarView.getAffix());
        }

        if (StringUtils.hasText(sysStarView.getStar())) {
            flag = false;
            updateWrapper.lambda().set(SysStarView::getStar,sysStarView.getStar());
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
        SysStarViewVO starView = null;
        List<SysStarViewVO> starViewVOList = LoginUserContext.getLoginUser().getStarViewVOList();
        for (SysStarViewVO starViewVO : starViewVOList) {
            if (starViewVO.getRouterPathKey().equals(sysStarView.getRouterPathKey())) {
                starViewVO.setAffix("1".equals(sysStarView.getAffix()))
                        .setStar("1".equals(sysStarView.getStar()));
                starView = starViewVO;
            }
        }
        LoginUserReset.resetStarViewList(starViewVOList);

        return starView;
    }
}
