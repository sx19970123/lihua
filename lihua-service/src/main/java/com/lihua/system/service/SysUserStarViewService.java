package com.lihua.system.service;

import com.lihua.model.security.SysUserStarViewVO;

import java.util.List;

public interface SysUserStarViewService {
    List<SysUserStarViewVO> selectByUserId(String userId);
}
