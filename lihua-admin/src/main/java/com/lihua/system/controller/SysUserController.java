package com.lihua.system.controller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.web.BaseController;
import com.lihua.system.model.SysBusinessUserDTO;
import com.lihua.system.service.SysBusinessJoinUserService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("system/user")
public class SysUserController extends BaseController {

    @Resource
    private SysBusinessJoinUserService sysBusinessJoinUserService;

    /**
     * 查询与对应业务关联的用户信息
     * @param sysBusinessUserDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("business/join/page")
    public String findBusinessUserPage(@RequestBody SysBusinessUserDTO sysBusinessUserDTO) {
        checkBusinessData(sysBusinessUserDTO);
        return success(sysBusinessJoinUserService.findBusinessUserPage(sysBusinessUserDTO));
    }

    /**
     * 查询没有与业务关联的用户信息
     * @param sysBusinessUserDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("business/notJoin/page")
    public String findNotBusinessUserPage(@RequestBody SysBusinessUserDTO sysBusinessUserDTO) {
        checkBusinessData(sysBusinessUserDTO);
        return success(sysBusinessJoinUserService.findNotBusinessUserPage(sysBusinessUserDTO));
    }

    /**
     * 解除用户与业务的绑定
     * @param sysBusinessUserDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("business/detach")
    public String detach(@RequestBody SysBusinessUserDTO sysBusinessUserDTO) {
        checkBusinessData(sysBusinessUserDTO);
        if (!StringUtils.hasText(sysBusinessUserDTO.getUserId())) {
            return error(ResultCodeEnum.PRIMARY_KEY_IS_EMPTY,"用户id不存在");
        }
        sysBusinessJoinUserService.detach(sysBusinessUserDTO);
        return success();
    }

    /**
     * 添加用户与业务的绑定
     * @param sysBusinessUserDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("business/associate")
    public String associate(@RequestBody SysBusinessUserDTO sysBusinessUserDTO) {
        checkBusinessData(sysBusinessUserDTO);
        if (sysBusinessUserDTO.getUserIdList() == null || sysBusinessUserDTO.getUserIdList().isEmpty()) {
            return error(ResultCodeEnum.PRIMARY_KEY_COLLECTION_IS_EMPTY,"用户id集合为空");
        }
        sysBusinessJoinUserService.associate(sysBusinessUserDTO);
        return success();
    }

    // 通用验证信息
    private void checkBusinessData(SysBusinessUserDTO sysBusinessUserDTO) {
        if (!StringUtils.hasText(sysBusinessUserDTO.getBusinessCode())) {
            throw new ServiceException("业务编码不存在");
        }
        if (!StringUtils.hasText(sysBusinessUserDTO.getBusinessId())) {
            throw new ServiceException("业务主键不存在");
        }
    }
}
