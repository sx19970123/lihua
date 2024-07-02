package com.lihua.system.model.vo;

import com.lihua.system.entity.SysDept;
import com.lihua.system.entity.SysPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptVO extends SysDept {

    /**
     * 岗位信息
     */
    private List<SysPost> sysPostList;
}
