package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.system.entity.SysUser;
import com.lihua.system.enums.BusinessUserEnums;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.system.model.SysBusinessUserDTO;
import com.lihua.system.service.SysBusinessJoinUserService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SysBusinessJoinUserServiceImpl implements SysBusinessJoinUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public IPage<SysUser> findBusinessUserPage(SysBusinessUserDTO sysBusinessUserDTO) {
        IPage<SysUser> ipage = new Page<>(sysBusinessUserDTO.getPageNum(), sysBusinessUserDTO.getPageSize());
        sysUserMapper.findBusinessUserPage(
                ipage,
                BusinessUserEnums.getTableNameByCode(sysBusinessUserDTO.getBusinessCode()),
                BusinessUserEnums.getFieldNameByCode(sysBusinessUserDTO.getBusinessCode()),
                sysBusinessUserDTO.getBusinessId()
        );
        return ipage;
    }

    @Override
    public IPage<SysUser> findNotBusinessUserPage(SysBusinessUserDTO sysBusinessUserDTO) {
        IPage<SysUser> ipage = new Page<>(sysBusinessUserDTO.getPageNum(), sysBusinessUserDTO.getPageSize());
        sysUserMapper.findNotBusinessUserPage(
                ipage,
                BusinessUserEnums.getTableNameByCode(sysBusinessUserDTO.getBusinessCode()),
                BusinessUserEnums.getFieldNameByCode(sysBusinessUserDTO.getBusinessCode()),
                sysBusinessUserDTO.getBusinessId()
        );
        return ipage;
    }

    @Override
    public void detach(SysBusinessUserDTO sysBusinessUserDTO) {
        sysUserMapper.detach(
                BusinessUserEnums.getTableNameByCode(sysBusinessUserDTO.getBusinessCode()),
                BusinessUserEnums.getFieldNameByCode(sysBusinessUserDTO.getBusinessCode()),
                sysBusinessUserDTO.getBusinessId(),
                sysBusinessUserDTO.getUserId());
    }

    @Override
    public void associate(SysBusinessUserDTO sysBusinessUserDTO) {
        sysUserMapper.associate(
                BusinessUserEnums.getTableNameByCode(sysBusinessUserDTO.getBusinessCode()),
                BusinessUserEnums.getFieldNameByCode(sysBusinessUserDTO.getBusinessCode()),
                sysBusinessUserDTO.getBusinessId(),
                LoginUserContext.getUserId(),
                LocalDateTime.now(),
                sysBusinessUserDTO.getUserIdList()
        );
    }
}
