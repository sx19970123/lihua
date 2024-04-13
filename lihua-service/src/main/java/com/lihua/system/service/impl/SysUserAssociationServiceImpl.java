package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.system.entity.SysUser;
import com.lihua.system.enums.UserAssociationEnums;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.system.model.SysUserAssociationDTO;
import com.lihua.system.service.SysUserAssociationService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SysUserAssociationServiceImpl implements SysUserAssociationService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public IPage<SysUser> findAssociatedUserPage(SysUserAssociationDTO sysUserAssociationDTO) {
        IPage<SysUser> ipage = new Page<>(sysUserAssociationDTO.getPageNum(), sysUserAssociationDTO.getPageSize());
        sysUserMapper.findAssociatedUserPage(
                ipage,
                UserAssociationEnums.getTableNameByCode(sysUserAssociationDTO.getCode()),
                UserAssociationEnums.getFieldNameByCode(sysUserAssociationDTO.getCode()),
                sysUserAssociationDTO.getId()
        );
        return ipage;
    }

    @Override
    public IPage<SysUser> findUnassociatedUserPage(SysUserAssociationDTO sysUserAssociationDTO) {
        IPage<SysUser> ipage = new Page<>(sysUserAssociationDTO.getPageNum(), sysUserAssociationDTO.getPageSize());
        sysUserMapper.findUnassociatedUserPage(
                ipage,
                UserAssociationEnums.getTableNameByCode(sysUserAssociationDTO.getCode()),
                UserAssociationEnums.getFieldNameByCode(sysUserAssociationDTO.getCode()),
                sysUserAssociationDTO.getId()
        );
        return ipage;
    }

    @Override
    public void detach(SysUserAssociationDTO sysUserAssociationDTO) {
        sysUserMapper.detach(
                UserAssociationEnums.getTableNameByCode(sysUserAssociationDTO.getCode()),
                UserAssociationEnums.getFieldNameByCode(sysUserAssociationDTO.getCode()),
                sysUserAssociationDTO.getId(),
                sysUserAssociationDTO.getUserId());
    }

    @Override
    public void associate(SysUserAssociationDTO sysUserAssociationDTO) {
        sysUserMapper.associate(
                UserAssociationEnums.getTableNameByCode(sysUserAssociationDTO.getCode()),
                UserAssociationEnums.getFieldNameByCode(sysUserAssociationDTO.getCode()),
                sysUserAssociationDTO.getId(),
                LoginUserContext.getUserId(),
                LocalDateTime.now(),
                sysUserAssociationDTO.getUserIdList()
        );
    }
}
