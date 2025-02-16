package com.lihua.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.model.security.CurrentRole;
import com.lihua.entity.system.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    // 根据用户id查询角色信息
    List<CurrentRole> selectSysRoleByUserId(String userId);
    // 查询角色信息（admin）
    List<CurrentRole> selectAllRole();
    // 根据角色id查询角色菜单关联表数据量(key：关联表对应列名，ids 对应列的id集合)
    Long selectRoleMenuCount(@Param("key") String key, @Param("ids") List<String> ids);
    // 根据角色id查询角色用户关联表数据量(key：关联表对应列名，ids 对应列的id集合)
    Long selectUserRoleCount(@Param("key") String key, @Param("ids") List<String> ids);
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
    // 查询所有角色，包含已停用状态
    List<SysRole> queryAllRole();
}
