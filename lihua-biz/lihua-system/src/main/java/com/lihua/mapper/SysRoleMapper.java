package com.lihua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.entity.SysRole;
import com.lihua.model.dto.SysRoleUserDTO;
import com.lihua.model.vo.SysRoleUserVO;
import com.lihua.security.model.CurrentRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    // 根据用户id查询角色信息
    List<CurrentRole> selectSysRoleByUserId(String userId);
    // 查询角色信息（admin）
    List<CurrentRole> selectAllRole();
    // 分页查询角色已授权用户
    IPage<SysRoleUserVO> selectUserPageByRoleId(IPage<SysRoleUserVO> page, @Param("roleId") String roleId, @Param("dto") SysRoleUserDTO dto);
    // 查询指定用户中已授权该角色的用户id（幂等过滤用）
    List<String> selectUserIdsByRoleIdAndUserIds(@Param("roleId") String roleId, @Param("userIds") List<String> userIds);
    // 向sys_user_role表中批量新增数据
    void insertUserRole(@Param("roleId") String roleId,@Param("userIds") List<String> userIds);
    // 根据角色id和用户id集合删除角色用户关联表数据
    void deleteUserRoleByRoleIdAndUserIds(@Param("roleId") String roleId, @Param("userIds") List<String> userIds);
    // 根据角色id查询角色菜单关联表数据量
    Long selectRoleMenuCountByRoleIds(@Param("roleIds") List<String> roleIds);
    // 根据角色id查询角色用户关联表数据量
    Long selectUserRoleCountByRoleIds(@Param("roleIds") List<String> roleIds);
    // 根据角色id 删除角色菜单关联表
    Long deleteRoleMenuByRoleId(String roleId);
    // 根据菜单id 删除角色菜单关联表
    Long deleteRoleMenuByMenuIds(List<String> menuIds);
    // 向sys_role_menu表中批量新增数据
    void insertRoleMenu(@Param("roleId") String roleId,@Param("menuIds") List<String> menuIds);
    // 根据roleId查询角色及绑定的菜单信息
    SysRole queryById(String roleId);
    // 根据用户id查询对应角色编码
    List<String> selectCodeByUserId(@Param("userId") String userId);
}
