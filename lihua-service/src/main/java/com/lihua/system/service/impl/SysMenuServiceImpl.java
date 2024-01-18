package com.lihua.system.service.impl;

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
    public List<SysMenuVO> selectSysMenuByLoginUser() {
        String userId = SecurityUtils.getUserId();
        List<SysMenuVO> sysMenuVOS = sysMenuMapper.selectPermsByUserId(userId);
        return TreeUtil.buildTree(sysMenuVOS);
    }
}
