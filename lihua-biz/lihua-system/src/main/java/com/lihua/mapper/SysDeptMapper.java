package com.lihua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.entity.SysDept;
import com.lihua.security.model.CurrentDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper extends BaseMapper<SysDept> {
    Long deptUserCount(@Param("ids") List<String> ids);

    List<CurrentDept> selectByUserId(@Param("userId") String userId);

    // 查询部门信息（admin）
    List<CurrentDept> selectAllDept(@Param("userId") String userId);
}
