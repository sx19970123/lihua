package com.lihua.system.controller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.web.BaseController;
import com.lihua.system.model.SysUserAssociationDTO;
import com.lihua.system.service.SysUserAssociationService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
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
    public String findAssociatedUserPage(@RequestBody SysUserAssociationDTO sysUserAssociationDTO) {
        checkBusinessData(sysUserAssociationDTO);
        return success(sysUserAssociationService.findAssociatedUserPage(sysUserAssociationDTO));
    }

    /**
     * 查询没有与业务关联的用户信息
     * @param sysUserAssociationDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("unAssociation/page")
    public String findUnassociatedUserPage(@RequestBody SysUserAssociationDTO sysUserAssociationDTO) {
        checkBusinessData(sysUserAssociationDTO);
        return success(sysUserAssociationService.findUnassociatedUserPage(sysUserAssociationDTO));
    }

    /**
     * 解除用户与业务的绑定
     * @param sysUserAssociationDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("association/detach")
    public String detach(@RequestBody SysUserAssociationDTO sysUserAssociationDTO) {
        checkBusinessData(sysUserAssociationDTO);
        if (!StringUtils.hasText(sysUserAssociationDTO.getUserId())) {
            return error(ResultCodeEnum.PRIMARY_KEY_IS_EMPTY,"用户id不存在");
        }
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
    public String associate(@RequestBody SysUserAssociationDTO sysUserAssociationDTO) {
        checkBusinessData(sysUserAssociationDTO);
        if (sysUserAssociationDTO.getUserIdList() == null || sysUserAssociationDTO.getUserIdList().isEmpty()) {
            return error(ResultCodeEnum.PRIMARY_KEY_COLLECTION_IS_EMPTY,"用户id集合为空");
        }
        sysUserAssociationService.associate(sysUserAssociationDTO);
        return success();
    }

    // 通用验证信息
    private void checkBusinessData(SysUserAssociationDTO sysUserAssociationDTO) {
        if (!StringUtils.hasText(sysUserAssociationDTO.getCode())) {
            throw new ServiceException("业务编码不存在");
        }
        if (!StringUtils.hasText(sysUserAssociationDTO.getId())) {
            throw new ServiceException("业务主键不存在");
        }
    }
}
