package com.lihua.mapper.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.lihua.model.security.CurrentUser;
import com.lihua.entity.system.SysUser;
import com.lihua.model.system.dto.SysUserDeptDTO;
import com.lihua.model.system.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface SysUserMapper extends BaseMapper<SysUser> {

    // 用户登陆查询
    CurrentUser loginSelect(@Param("username") String username);

    // 列表查询
    IPage<SysUserVO> queryPage(@Param("iPage") IPage<SysUserVO> iPage, @Param(Constants.WRAPPER) QueryWrapper<SysUser> queryWrapper);

    // 根据用户id集合查询对应的部门
    List<SysUserDeptDTO> queryUserDeptByUserIds(@Param("userIds") List<String> userIds);

    // 根据id查询用户全部信息
    SysUserVO queryById(@Param("id") String id);

    // 查询导出用户列表
    List<SysUserVO> queryExportData(@Param(Constants.WRAPPER) QueryWrapper<SysUser> queryWrapper);

    // 查询已存在的用户名
    Set<String> queryUsername(@Param("usernameSet") Set<String> usernameSet);

    // 查询已存在的电话号码
    Set<String> queryPhoneNumber(@Param("phoneNumberSet") Set<String> phoneNumberSet);

    // 查询已存在的邮箱
    Set<String> queryEmail(@Param("emailSet") Set<String> emailSet);

    // 根据deptId查询用户
    List<SysUser> queryOptionByDeptId(@Param("deptId") String deptId);

    // 根据用户id获取用户头像/昵称等信息
    List<SysUser> queryOptionByUserIds(@Param("userIdList") List<String> userIdList);
}
