package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.model.security.RouterVO;
import com.lihua.system.entity.SysMenu;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.model.SysMenuDTO;
import com.lihua.system.service.SysMenuService;
import com.lihua.utils.tree.TreeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public IPage<SysMenu> findPage(SysMenuDTO sysMenuDTO) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        IPage<SysMenu> ipage = new Page<>(sysMenuDTO.getPageNum(), sysMenuDTO.getPageSize());

        if (StringUtils.hasText(sysMenuDTO.getLabel())) {
            queryWrapper.lambda().like(SysMenu::getLabel,sysMenuDTO.getLabel());
        }

        if (StringUtils.hasText(sysMenuDTO.getStatus())) {
            queryWrapper.lambda().eq(SysMenu::getLabel,sysMenuDTO.getStatus());
        }

        sysMenuMapper.selectPage(ipage,queryWrapper);
        return ipage;
    }

    @Override
    public SysMenu findById(String menuId) {
        return null;
    }

    @Override
    public String save(SysMenu sysMenu) {
        return null;
    }

    @Override
    public void deleteByIds(List<String> ids) {

    }

    @Override
    public List<RouterVO> selectSysMenuByLoginUserId(String userId) {
        List<RouterVO> sysMenuVOS = sysMenuMapper.selectPermsByUserId(userId);
        // 设置name属性
        sysMenuVOS.forEach(item -> item.setName(com.lihua.utils.string.StringUtils.initialUpperCase(item.getPath().replaceFirst("/",""))));
        return TreeUtil.buildTree(sysMenuVOS);
    }
}
