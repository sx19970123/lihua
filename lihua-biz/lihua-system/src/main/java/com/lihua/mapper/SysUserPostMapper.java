package com.lihua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.entity.SysUserPost;
import com.lihua.model.vo.SysPostVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface SysUserPostMapper extends BaseMapper<SysUserPost> {
    // 根据用户id查询用户下对应的岗位
    List<SysPostVO> queryPostByUserIds(@Param("userIds") Collection<String> userIds);
}
