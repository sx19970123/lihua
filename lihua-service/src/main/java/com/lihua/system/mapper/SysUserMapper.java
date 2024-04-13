package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUserVO selectByUsername(String username);

    // 查询与业务相关联的用户信息
    IPage<SysUser> findAssociatedUserPage(@Param("iPage") IPage<SysUser> iPage,
                                       @Param("tableName") String tableName,
                                       @Param("fieldName") String fieldName,
                                       @Param("id") String id);
    // 查询与业务没有关联的用户信息
    IPage<SysUser> findUnassociatedUserPage(@Param("iPage") IPage<SysUser> iPage,
                                       @Param("tableName") String tableName,
                                       @Param("fieldName") String fieldName,
                                       @Param("id") String id);

    // 用户解除绑定
    void detach(@Param("tableName") String tableName,
                @Param("fieldName") String fieldName,
                @Param("id") String id,
                @Param("userId") String userId);
    // 用户与业务绑定
    void associate(
            @Param("tableName") String tableName,
            @Param("fieldName") String fieldName,
            @Param("id") String id,
            @Param("createId") String createId,
            @Param("createTime") LocalDateTime createTime,
            @Param("userIdList") List<String> userIdList);
}
