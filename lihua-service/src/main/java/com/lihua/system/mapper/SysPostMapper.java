package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.lihua.system.entity.SysPost;
import com.lihua.system.model.SysPostVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPostMapper extends BaseMapper<SysPost> {

    IPage<SysPostVO> findPage(@Param("iPage") IPage<SysPostVO> iPage,
                              @Param(Constants.WRAPPER) QueryWrapper<SysPost> queryWrapper);

    Long postUserCount(List<String> ids);
}
