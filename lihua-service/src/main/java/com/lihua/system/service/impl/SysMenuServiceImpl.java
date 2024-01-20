package com.lihua.system.service.impl;

import com.lihua.model.MetaVO;
import com.lihua.model.RouteVO;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.model.SysMenuVO;
import com.lihua.system.service.SysMenuService;
import com.lihua.utils.string.StringUtils;
import com.lihua.utils.tree.TreeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenuVO> selectSysMenuByLoginUserId(String userId) {
        List<SysMenuVO> sysMenuVOS = sysMenuMapper.selectPermsByUserId(userId);
        return TreeUtil.buildTree(sysMenuVOS);
    }

    @Override
    public List<RouteVO> initMetaRouteInfo(String userId) {
        List<SysMenuVO> sysMenuVOS = sysMenuMapper.selectPermsByUserId(userId);
        List<RouteVO> routeVOS = new ArrayList<>();
        sysMenuVOS.forEach(menu -> {
            // 处理无父级菜单页面
            if ("0".equals(menu.getParentId()) && "page".equals(menu.getType())) {
                routeVOS.add(handleRootPage(menu));
            }
            // 菜单转为 route
            routeVOS.add(intiRoute(menu));
        });
        return TreeUtil.buildTree(routeVOS);
    }



    /**
     * 通过 SysMenuVO 组合 RouteVO
     * @param menuVO
     * @return
     */
    private RouteVO intiRoute(SysMenuVO menuVO) {
        RouteVO routeVO = new RouteVO();
        MetaVO metaVO = new MetaVO();
        metaVO
            .setLabel(menuVO.getLabel())
            .setTitle(menuVO.getTitle())
            .setIcon(menuVO.getIcon())
            .setNoCache("1".equals(menuVO.getNoCache()))
            .setLink(menuVO.getLinkPath());
        routeVO
            .setId(menuVO.getId())
            .setParentId(menuVO.getParentId())
            .setName(StringUtils.initialUpperCase(menuVO.getRouterPath()))
            .setPath(menuVO.getRouterPath())
            .setComponent(getComponent(menuVO))
            .setMeta(metaVO);
        return routeVO;
    }

    /**
     * 获取数据组件类型：
     *  页面直接返回存储的组件路径
     *  顶级菜单返回 Layout
     *  中间层级菜单返回 MiddleView
     */
    private String getComponent(SysMenuVO menuVO) {
        // todo 后期换做数据字典维护
        if ("page".equals(menuVO.getType())) {
            return menuVO.getComponentPath();
        }
        return menuVO.getParentId().equals("0") ? "Layout" : "MiddleView";
    }

    private RouteVO handleRootPage(SysMenuVO menuVO) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        RouteVO routeVO = new RouteVO();
        routeVO
            .setId(uuid)
            .setParentId("0")
            .setPath("/")
            .setComponent("Layout")
            .setMeta(new MetaVO());
        menuVO.setParentId(uuid);
        return routeVO;
    }
}
