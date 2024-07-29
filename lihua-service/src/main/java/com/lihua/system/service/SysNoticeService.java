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
     * @param sysNoticeDTO
     * @return
     */
    IPage<SysNotice> findPage(SysNoticeDTO sysNoticeDTO);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    SysNoticeVO findById(String id);

    /**
     * 保存消息通知
     * @param sysNoticeDTO
     * @return
     */
    String save(SysNoticeDTO sysNoticeDTO);

    /**
     * 发布消息通知
     * @param id
     * @return
     */
    String release(String id);

    /**
     * 撤销消息通知
     * @param id
     * @return
     */
    String revoke(String id);

    /**
     * 删除消息
     * @param ids
     */
    void deleteByIds(List<String> ids);

    /**
     * 用户查询消息通知
     * @param userId
     * @param sysNoticeDTO
     * @return
     */
    IPage<SysUserNoticeVO> findListByUserId(String userId, SysNoticeDTO sysNoticeDTO);
}
