package com.lihua.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.entity.SysAppVersion;
import com.lihua.model.dto.SysAppVersionDTO;
import com.lihua.model.vo.SysAppVersionVO;

import java.util.List;

/**
 * App 版本发布记录 Service
 */
public interface SysAppVersionService {

    /**
     * 分页查询
     */
    IPage<SysAppVersion> queryPage(SysAppVersionDTO dto);

    /**
     * 根据 id 查询详情
     */
    SysAppVersion queryById(String id);

    /**
     * 新增/编辑版本（新建强制草稿，已发布记录禁止编辑）
     */
    String saveOrUpdate(SysAppVersionDTO dto);

    /**
     * 发布版本（0 草稿 → 1 已发布）
     */
    String publish(String id);

    /**
     * 下线版本（1 已发布 → 2 已下线）
     */
    String offline(String id);

    /**
     * 批量删除版本（已发布版本禁止删除）
     */
    void deleteByIds(List<String> ids);

    /**
     * App 检查更新：比较当前客户端 versionCode 与指定平台最新已发布版本，
     * 无更新返回 null；有更新时计算实际生效的下载地址与更新方式（apk/wgt/link）
     */
    SysAppVersionVO checkUpdate(String platform, Integer currentVersionCode);
}
