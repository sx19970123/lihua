package com.lihua.system.service;

import com.lihua.system.entity.SysUserNotice;

import java.util.List;

public interface SysUserNoticeService {

    void save(List<SysUserNotice> sysUserNotices);

    void deleteByNoticeIds(List<String> noticeIds);
}
