package com.lihua.system.service;

import com.lihua.model.security.SysStarViewVO;
import com.lihua.system.entity.SysStarView;

import java.util.List;

public interface SysStarViewService {
    List<SysStarViewVO> selectByUserId(String userId);

    SysStarViewVO save(SysStarView sysStarView);
}
