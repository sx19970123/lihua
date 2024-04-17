package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.model.SysUserAssociationDTO;
import com.lihua.system.model.validation.UserAssociationValidation;
import com.lihua.system.service.SysUserAssociationService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/user")
public class SysUserController extends BaseController {

    @Resource
    private SysUserAssociationService sysUserAssociationService;

    /**
     * 查询与对应业务关联的用户信息
     * @param sysUserAssociationDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("association/page")
    public String findAssociatedUserPage(@RequestBody @Validated(UserAssociationValidation.AssociationValidation.class) SysUserAssociationDTO sysUserAssociationDTO) {
        return success(sysUserAssociationService.findAssociatedUserPage(sysUserAssociationDTO));
    }

    /**
     * 查询没有与业务关联的用户信息
     * @param sysUserAssociationDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("unAssociation/page")
    public String findUnassociatedUserPage(@RequestBody @Validated(UserAssociationValidation.AssociationValidation.class) SysUserAssociationDTO sysUserAssociationDTO) {
        return success(sysUserAssociationService.findUnassociatedUserPage(sysUserAssociationDTO));
    }

    /**
     * 解除用户与业务的绑定
     * @param sysUserAssociationDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("association/detach")
    public String detach(@RequestBody @Validated({UserAssociationValidation.AssociationValidation.class,UserAssociationValidation.DetachAssociationValidation.class}) SysUserAssociationDTO sysUserAssociationDTO) {
        sysUserAssociationService.detach(sysUserAssociationDTO);
        return success();
    }

    /**
     * 添加用户与业务的绑定
     * @param sysUserAssociationDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("association")
    public String associate(@RequestBody @Validated({UserAssociationValidation.AssociationValidation.class,UserAssociationValidation.AssociateAssociationValidation.class}) SysUserAssociationDTO sysUserAssociationDTO) {
        sysUserAssociationService.associate(sysUserAssociationDTO);
        return success();
    }

}
