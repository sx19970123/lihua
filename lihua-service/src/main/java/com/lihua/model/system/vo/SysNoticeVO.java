package com.lihua.model.system.vo;

import com.lihua.entity.system.SysNotice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysNoticeVO extends SysNotice {

    /**
     * 指定用户
     */
    private List<String> userIdList = new ArrayList<>();

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 发布用户
     */
    private String releaseUser;
}
