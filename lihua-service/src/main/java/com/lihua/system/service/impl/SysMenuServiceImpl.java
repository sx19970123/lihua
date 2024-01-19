package com.lihua.system.service.impl;

import com.lihua.model.RouteVO;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.model.SysMenuVO;
import com.lihua.system.service.SysMenuService;
import com.lihua.utils.security.SecurityUtils;
import com.lihua.utils.tree.TreeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
        sysMenuMapper.selectPermsByUserId(userId);
        sysMenuMapper.selectPermsByUserId(userId);

        return null;
    }
}
