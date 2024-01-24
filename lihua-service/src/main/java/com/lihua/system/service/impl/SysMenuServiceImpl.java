package com.lihua.system.service.impl;

import com.lihua.model.security.RouterVO;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.service.SysMenuService;
import com.lihua.utils.string.StringUtils;
import com.lihua.utils.tree.TreeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<RouterVO> selectSysMenuByLoginUserId(String userId) {
        List<RouterVO> sysMenuVOS = sysMenuMapper.selectPermsByUserId(userId);
        // 设置name属性
        sysMenuVOS.forEach(item -> item.setName(StringUtils.initialUpperCase(item.getPath().replaceFirst("/",""))));
        return TreeUtil.buildTree(sysMenuVOS);
    }
}
