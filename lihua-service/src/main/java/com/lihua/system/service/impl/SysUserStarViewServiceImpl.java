package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.model.security.RouterVO;
import com.lihua.model.security.SysUserStarViewVO;
import com.lihua.system.entity.SysUserStarView;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.mapper.SysUserStarViewMapper;
import com.lihua.system.service.SysUserStarViewService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class SysUserStarViewServiceImpl implements SysUserStarViewService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private SysUserStarViewMapper sysUserStarViewMapper;

    @Override
    public List<SysUserStarViewVO> selectByUserId(String userId) {
        // 获取当前用户菜单数据
        List<RouterVO> routerVOList = sysMenuMapper.selectPermsByUserId(userId);

        // 获取收藏菜单数据
        QueryWrapper<SysUserStarView> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserStarView::getUserId,userId);
        List<SysUserStarView> sysUserStarViews = sysUserStarViewMapper.selectList(queryWrapper);

        // 数据组合
        List<SysUserStarViewVO> viewVOS = new ArrayList<>();
        routerVOList
                .stream()
                .filter(route -> "page".equals(route.getType()))
                .forEach(route -> {
                    SysUserStarViewVO sysUserStarViewVO = new SysUserStarViewVO();
                    sysUserStarViewVO
                            .setLabel(route.getMeta().getLabel())
                            .setIcon(route.getMeta().getIcon())
                            .setRouterPathKey(route.getKey());
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
}
