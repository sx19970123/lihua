package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.entity.SysNotice;
import com.lihua.system.model.dto.SysNoticeDTO;
import com.lihua.system.model.vo.SysNoticeVO;
import com.lihua.system.model.vo.SysUserNoticeVO;

import java.util.List;

public interface SysNoticeService {

    /**
     * 分页查询
     */
    IPage<SysNotice> queryPage(SysNoticeDTO sysNoticeDTO);

    /**
     * 根据主键查询
     */
    SysNoticeVO queryById(String id);

    /**
     * 预览通知公告
     */
    SysNoticeVO preview(String id);

    /**
     * 保存消息通知
     */
    String save(SysNoticeDTO sysNoticeDTO);

    /**
     * 发布消息通知
     */
    String release(String id);

    /**
     * 撤销消息通知
     */
    String revoke(String id);

    /**
     * 删除消息
     */
    void deleteByIds(List<String> ids);

    /**
     * 用户查询消息通知
     */
    IPage<SysUserNoticeVO> queryListByUserId(String userId, SysNoticeDTO sysNoticeDTO);
}
