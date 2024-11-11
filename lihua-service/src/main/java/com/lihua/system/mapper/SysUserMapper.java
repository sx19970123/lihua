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
import java.util.Set;

public interface SysUserMapper extends BaseMapper<SysUser> {

    // 用户登陆查询
    CurrentUser loginSelect(@Param("username") String username);

    // 列表查询
    IPage<SysUserVO> findPage(@Param("iPage") IPage<SysUserVO> iPage, @Param(Constants.WRAPPER) QueryWrapper<SysUser> queryWrapper);

    // 根据用户id集合查询对应的部门
    List<SysUserDeptDTO> findUserDeptByUserIds(@Param("userIds") List<String> userIds);

    // 根据id查询用户全部信息
    SysUserVO findById(@Param("id") String id);

    // 查询导出用户列表
    List<SysUserVO> findExportData(@Param(Constants.WRAPPER) QueryWrapper<SysUser> queryWrapper);

    // 查询已存在的用户名
    Set<String> findUsername(@Param("usernameSet") Set<String> usernameSet);

    // 查询已存在的电话号码
    Set<String> findPhoneNumber(@Param("phoneNumberSet") Set<String> phoneNumberSet);

    // 查询已存在的邮箱
    Set<String> findEmail(@Param("emailSet") Set<String> emailSet);

    // 根据deptId查询用户
    List<SysUser> findOptionByDeptId(@Param("deptId") String deptId);

    // 根据用户id获取用户头像/昵称等信息
    List<SysUser> findOptionByUserIds(@Param("userIdList") List<String> userIdList);

    // 检查用户名是否存在
    String checkUserName(String username);
}
