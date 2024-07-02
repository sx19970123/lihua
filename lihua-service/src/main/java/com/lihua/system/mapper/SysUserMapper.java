package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.lihua.model.security.CurrentUser;
import com.lihua.system.entity.SysUser;
import com.lihua.system.model.dto.SysUserDeptDTO;
import com.lihua.system.model.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {

    // 用户登陆查询
    CurrentUser loginSelect(@Param("username") String username);

    IPage<SysUserVO> findPage(@Param("iPage") IPage<SysUserVO> iPage, @Param(Constants.WRAPPER) QueryWrapper<SysUser> queryWrapper);

    List<SysUserDeptDTO> findUserDeptByUserIds(@Param("userIds") List<String> userIds);

    // 根据id查询用户全部信息
    SysUserVO findById(@Param("id") String id);

    // 根据username、phoneNumber、email 查询符合条件的用户
    List<SysUser> checkUserData(@Param("username") String username,@Param("phoneNumber") String phoneNumber,@Param("email") String email);

    // 查询导出用户列表
    List<SysUserVO> findExportData(@Param(Constants.WRAPPER) QueryWrapper<SysUser> queryWrapper);

    // 查询已存在的用户名
    List<String> findUsername(@Param("usernameList") List<String> usernameList);
}
