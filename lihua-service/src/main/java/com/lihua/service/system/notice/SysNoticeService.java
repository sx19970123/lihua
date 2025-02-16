package com.lihua.service.system.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.entity.system.SysNotice;
import com.lihua.model.system.dto.SysNoticeDTO;
import com.lihua.model.system.vo.SysNoticeVO;
import com.lihua.model.system.vo.SysUserNoticeVO;

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
