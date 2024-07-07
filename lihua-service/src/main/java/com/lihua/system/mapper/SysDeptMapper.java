package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.model.security.CurrentDept;
import com.lihua.system.entity.SysDept;
import com.lihua.system.model.vo.SysDeptVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface SysDeptMapper extends BaseMapper<SysDept> {
    Long deptUserCount(@Param("ids") List<String> ids);

    List<CurrentDept> selectByUserId(@Param("userId") String userId);

    // 查询部门信息（admin）
    List<CurrentDept> selectAllDept(@Param("userId") String userId);

    // 查询所有部门及其岗位（包括停用）
    List<SysDeptVO> findAllDept();

    // 查询对应部门名称在数据库中是否存在
    Set<String> findDeptNameByNames(@Param("deptNameSet") Set<String> deptNameSet);

    // 查询对应部门编码在数据库中是否存在
    Set<String> findDeptCodeByCodes(@Param("deptCodeSet") Set<String> deptCodeSet);
}
