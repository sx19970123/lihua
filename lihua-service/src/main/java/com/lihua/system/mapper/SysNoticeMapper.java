package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.entity.SysNotice;
import com.lihua.system.model.vo.SysUserNoticeVO;
import org.apache.ibatis.annotations.Param;

public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    IPage<SysUserNoticeVO> findListByUserId(@Param("userId") String userId, @Param("iPage") IPage<SysUserNoticeVO> iPage);
}
