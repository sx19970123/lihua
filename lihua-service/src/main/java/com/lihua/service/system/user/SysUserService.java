package com.lihua.service.system.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.model.excel.ExcelImportResult;
import com.lihua.entity.system.SysUser;
import com.lihua.model.system.dto.ResetPasswordDTO;
import com.lihua.model.system.dto.SysUserDTO;
import com.lihua.model.system.vo.SysUserVO;

import java.util.List;

public interface SysUserService {

    /**
     * 用户列表页查询
     */
    IPage<SysUserVO> queryPage(SysUserDTO sysUserDTO);

    /**
     * 根据id查询用户信息
     */
    SysUserVO queryById(String id);

    /**
     * 用户信息保存
     */
    String save(SysUserDTO sysUserDTO);

    /**
     * 删除用户信息
     */
    void deleteByIds(List<String> ids);

    /**
     * 修改用户状态
     * @param id 用户id
     * @param currentStatus 当前用户状态
     * @return 更新后的用户状态
     */
    String updateStatus(String id, String currentStatus);

    /**
     * 导出excel
     */
    String exportExcel(SysUserDTO sysUserDTO);

    /**
     * excel 批量导入
     */
    ExcelImportResult importExcel(List<SysUserVO> sysUserVOS);

    /**
     * 系统用户选项（根据部门选择）
     */
    List<SysUser> userOption(String deptId);

    /**
     * 系统用户选项（根据用户id集合）
     */
    List<SysUser> userOption(List<String> userIdList);

    /**
     * 获取全部用户 id，包括停用用户
     */
    List<String> queryAllUserIds();

    /**
     * 重置密码
     */
    String resetPassword(ResetPasswordDTO resetPasswordDTO);
}
