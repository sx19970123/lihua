package com.lihua.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.model.security.CurrentDept;
import com.lihua.entity.system.SysDept;
import com.lihua.model.system.vo.SysDeptVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface SysDeptMapper extends BaseMapper<SysDept> {
    Long deptUserCount(@Param("ids") List<String> ids);

    List<CurrentDept> selectByUserId(@Param("userId") String userId);

    // 查询部门信息（admin）
    List<CurrentDept> selectAllDept(@Param("userId") String userId);

    // 查询所有部门及其岗位（包括停用）
    List<SysDeptVO> queryAllDept();

    // 查询对应部门名称在数据库中是否存在
    Set<String> queryDeptNameByNames(@Param("deptNameSet") Set<String> deptNameSet);

    // 查询对应部门编码在数据库中是否存在
    Set<String> queryDeptCodeByCodes(@Param("deptCodeSet") Set<String> deptCodeSet);
}
