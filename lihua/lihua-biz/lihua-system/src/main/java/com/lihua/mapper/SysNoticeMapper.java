package com.lihua.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.lihua.entity.SysNotice;
import com.lihua.model.vo.SysNoticeVO;
import com.lihua.model.vo.SysUserNoticeVO;
import org.apache.ibatis.annotations.Param;

public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    IPage<SysUserNoticeVO> queryListByUserId(@Param("iPage") IPage<SysUserNoticeVO> iPage,
                                            @Param(Constants.WRAPPER) QueryWrapper<SysUserNoticeVO> queryWrapper);

    SysNoticeVO preview(@Param("id") String id);
}
