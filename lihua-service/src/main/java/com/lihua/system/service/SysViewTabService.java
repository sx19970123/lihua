package com.lihua.system.service;

import com.lihua.model.security.SysViewTabVO;
import com.lihua.system.entity.SysViewTab;

import java.util.List;

public interface SysViewTabService {
    List<SysViewTabVO> selectByUserId(String userId);

    SysViewTabVO save(SysViewTab sysViewTab);
}
