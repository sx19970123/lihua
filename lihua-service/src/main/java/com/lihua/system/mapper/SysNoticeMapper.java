package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.system.entity.SysNotice;
import com.lihua.system.model.vo.SysNoticeVO;
import org.apache.ibatis.annotations.Param;

public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    SysNoticeVO findById(@Param("id") String id);
}
