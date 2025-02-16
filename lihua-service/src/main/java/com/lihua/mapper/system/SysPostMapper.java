package com.lihua.mapper.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.lihua.model.security.CurrentPost;
import com.lihua.entity.system.SysPost;
import com.lihua.model.system.vo.SysPostVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPostMapper extends BaseMapper<SysPost> {

    IPage<SysPostVO> queryPage(@Param("iPage") IPage<SysPostVO> iPage,
                              @Param(Constants.WRAPPER) QueryWrapper<SysPost> queryWrapper);

    List<SysPostVO> queryPage(@Param(Constants.WRAPPER) QueryWrapper<SysPost> queryWrapper);

    Long postUserCount(@Param("ids") List<String> ids);

    List<CurrentPost> selectByUserId(@Param("userId") String userId);

    // 查询岗位信息（admin）
    List<CurrentPost> selectAllPost();
}
