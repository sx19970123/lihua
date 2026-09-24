package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.common.utils.url.AttachmentUrlUtils;
import com.lihua.entity.SysAppVersion;
import com.lihua.enums.AppPlatformEnum;
import com.lihua.enums.AppVersionEnableWgtEnum;
import com.lihua.enums.AppVersionStatusEnum;
import com.lihua.mapper.SysAppVersionMapper;
import com.lihua.model.dto.SysAppVersionDTO;
import com.lihua.model.vo.SysAppVersionVO;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.service.SysAppVersionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lihua.common.utils.string.StringUtils;

import java.net.URI;
import java.util.List;
import java.util.Locale;

/**
 * App 版本发布记录 Service 实现
 * <p>状态机：新建强制草稿；发布写发布时间；下线；
 * 已发布记录禁止编辑与删除（须先下线）。
 * <p>版本比较不看 versionName（仅展示），纯整数比较 versionCode；
 * Android 热更新要求 (currentVersionCode, latest.versionCode] 区间内全部版本支持 wgt，
 * 任一版本不支持则降级整包更新。
 * <p>平台/状态/热更新开关均为字典值，读写一律经字典枚举（app_version_platform/app_version_status/app_version_enable_wgt），不留字面量
 */
@Slf4j
@Service
public class SysAppVersionServiceImpl implements SysAppVersionService {

    @Resource
    private SysAppVersionMapper sysAppVersionMapper;

    @Override
    public IPage<SysAppVersion> queryPage(SysAppVersionDTO dto) {
        IPage<SysAppVersion> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<SysAppVersion> queryWrapper = new LambdaQueryWrapper<>();

        // 平台
        if (StringUtils.hasText(dto.getPlatform())) {
            queryWrapper.eq(SysAppVersion::getPlatform, dto.getPlatform());
        }
        // 状态
        if (StringUtils.hasText(dto.getStatus())) {
            queryWrapper.eq(SysAppVersion::getStatus, dto.getStatus());
        }
        // 版本名称模糊搜索
        if (StringUtils.hasText(dto.getVersionName())) {
            queryWrapper.like(SysAppVersion::getVersionName, dto.getVersionName());
        }

        queryWrapper.orderByDesc(SysAppVersion::getVersionCode);
        return sysAppVersionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public SysAppVersion queryById(String id) {
        return sysAppVersionMapper.selectById(id);
    }

    @Override
    @Transactional
    public String saveOrUpdate(SysAppVersionDTO dto) {
        normalizeAndValidatePackageUrls(dto);

        // 编辑场景：已发布记录不可编辑
        if (StringUtils.hasText(dto.getId())) {
            String status = getStatus(dto.getId());
            if (AppVersionStatusEnum.PUBLISHED.getValue().equals(status)) {
                throw new ServiceException("已发布版本无法编辑");
            }
            SysAppVersion entity = new SysAppVersion();
            BeanUtils.copyProperties(dto, entity);
            sysAppVersionMapper.updateById(entity);
            return dto.getId();
        }

        // 新建强制为草稿状态
        dto.setStatus(AppVersionStatusEnum.DRAFT.getValue());
        SysAppVersion entity = new SysAppVersion();
        BeanUtils.copyProperties(dto, entity);
        sysAppVersionMapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public String publish(String id) {
        String status = getStatus(id);
        if (AppVersionStatusEnum.PUBLISHED.getValue().equals(status)) {
            throw new ServiceException("该版本已发布");
        }

        UpdateWrapper<SysAppVersion> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SysAppVersion::getId, id)
                .set(SysAppVersion::getStatus, AppVersionStatusEnum.PUBLISHED.getValue())
                .set(SysAppVersion::getPublishTime, DateUtils.now())
                .set(SysAppVersion::getUpdateId, LoginUserContext.getUserId())
                .set(SysAppVersion::getUpdateTime, DateUtils.now());
        sysAppVersionMapper.update(updateWrapper);
        return id;
    }

    @Override
    @Transactional
    public String offline(String id) {
        String status = getStatus(id);
        if (!AppVersionStatusEnum.PUBLISHED.getValue().equals(status)) {
            throw new ServiceException("仅已发布版本可下线");
        }

        UpdateWrapper<SysAppVersion> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SysAppVersion::getId, id)
                .set(SysAppVersion::getStatus, AppVersionStatusEnum.OFFLINE.getValue())
                .set(SysAppVersion::getUpdateId, LoginUserContext.getUserId())
                .set(SysAppVersion::getUpdateTime, DateUtils.now());
        sysAppVersionMapper.update(updateWrapper);
        return id;
    }

    @Override
    @Transactional
    public void deleteByIds(List<String> ids) {
        LambdaQueryWrapper<SysAppVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAppVersion::getStatus, AppVersionStatusEnum.PUBLISHED.getValue()).in(SysAppVersion::getId, ids);
        Long count = sysAppVersionMapper.selectCount(queryWrapper);

        if (count > 0) {
            throw new ServiceException("已发布版本无法删除，请先下线");
        }

        sysAppVersionMapper.deleteByIds(ids);
    }

    @Override
    public SysAppVersionVO checkUpdate(String platform, Integer currentVersionCode) {
        // 1. 查 platform 下 versionCode 最大且已发布的记录
        SysAppVersion latest = queryLatestPublished(platform);
        if (latest == null || latest.getVersionCode() <= currentVersionCode) {
            return null;
        }

        SysAppVersionVO vo = new SysAppVersionVO();
        BeanUtils.copyProperties(latest, vo);

        // 2. iOS/鸿蒙直接返回（App 用 download_url 跳转应用市场或分发页），effectiveType='link'
        if (isLinkPlatform(platform)) {
            vo.setEffectiveDownloadUrl(toEffectiveDownloadUrl(latest.getDownloadUrl()));
            vo.setEffectiveType("link");
            return vo;
        }

        // 3. Android：wgt 连续性检查
        //    查 (currentVersionCode, latest.versionCode] 区间内所有已发布记录
        LambdaQueryWrapper<SysAppVersion> rangeWrapper = new LambdaQueryWrapper<>();
        rangeWrapper.eq(SysAppVersion::getPlatform, platform)
                .eq(SysAppVersion::getStatus, AppVersionStatusEnum.PUBLISHED.getValue())
                .gt(SysAppVersion::getVersionCode, currentVersionCode)
                .le(SysAppVersion::getVersionCode, latest.getVersionCode());
        List<SysAppVersion> range = sysAppVersionMapper.selectList(rangeWrapper);

        // 区间内任一版本 enable_wgt != '1' → 整包更新；否则热更新
        boolean allSupportWgt = range.stream().allMatch(v -> AppVersionEnableWgtEnum.YES.getValue().equals(v.getEnableWgt()));
        if (allSupportWgt) {
            vo.setEffectiveDownloadUrl(toEffectiveDownloadUrl(latest.getWgtDownloadUrl()));
            vo.setEffectiveType("wgt");
        } else {
            vo.setEffectiveDownloadUrl(toEffectiveDownloadUrl(latest.getDownloadUrl()));
            vo.setEffectiveType("apk");
        }
        return vo;
    }

    /**
     * 查询指定平台 versionCode 最大且已发布的版本
     */
    private SysAppVersion queryLatestPublished(String platform) {
        LambdaQueryWrapper<SysAppVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAppVersion::getPlatform, platform)
                .eq(SysAppVersion::getStatus, AppVersionStatusEnum.PUBLISHED.getValue())
                .orderByDesc(SysAppVersion::getVersionCode)
                .last("LIMIT 1");
        return sysAppVersionMapper.selectOne(queryWrapper);
    }

    /**
     * 外链跳转型平台（iOS/鸿蒙：无整包与热更安装链路，download_url 指向应用市场或分发页）
     */
    private boolean isLinkPlatform(String platform) {
        return AppPlatformEnum.IOS.getValue().equals(platform) || AppPlatformEnum.HARMONYOS.getValue().equals(platform);
    }

    /**
     * 组装实际生效的下载地址：HTTP(S) 绝对直链原样返回；
     * 附件 path 经 AttachmentUrlUtils 统一组装为公开附件下载相对链（App 端补全自身 baseURL 后访问）
     */
    private String toEffectiveDownloadUrl(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String lower = value.toLowerCase(Locale.ROOT);
        if (lower.startsWith("http://") || lower.startsWith("https://")) {
            return value;
        }
        return AttachmentUrlUtils.resolvePublicUrl(value);
    }

    /**
     * 规范化并校验安装包地址（downloadUrl 非空已由 DTO @NotBlank 兜住）
     */
    private void normalizeAndValidatePackageUrls(SysAppVersionDTO dto) {
        dto.setDownloadUrl(StringUtils.trimToNull(dto.getDownloadUrl()));
        dto.setWgtDownloadUrl(StringUtils.trimToNull(dto.getWgtDownloadUrl()));

        if (AppPlatformEnum.ANDROID.getValue().equals(dto.getPlatform())) {
            validatePackagePath(dto.getDownloadUrl(), ".apk", "APK 地址");
            validateAndroidWgtUrl(dto);
            return;
        }

        // 平台合法性已由 DTO @Pattern(AppPlatformEnum.REGEX) 兜住，此处仅剩 android/外链平台两分支
        if (isLinkPlatform(dto.getPlatform())) {
            validateHttpUrl(dto.getDownloadUrl());
            dto.setEnableWgt(AppVersionEnableWgtEnum.NO.getValue());
            dto.setWgtDownloadUrl(null);
            return;
        }
    }

    /**
     * 校验 Android 热更新包地址
     */
    private void validateAndroidWgtUrl(SysAppVersionDTO dto) {
        if (AppVersionEnableWgtEnum.YES.getValue().equals(dto.getEnableWgt())) {
            if (!StringUtils.hasText(dto.getWgtDownloadUrl())) {
                throw new ServiceException("启用热更新时必须上传 WGT 包");
            }
            validatePackagePath(dto.getWgtDownloadUrl(), ".wgt", "WGT 地址");
            return;
        }

        // 关闭热更新时清空 wgt 地址，避免脏数据
        dto.setEnableWgt(AppVersionEnableWgtEnum.NO.getValue());
        dto.setWgtDownloadUrl(null);
    }

    /**
     * 校验附件 path（Android 安装包/热更包恒为附件上传产物，相对对象键）：
     * 拒绝根路径、反斜杠与目录穿越，并按扩展名收敛文件类型
     */
    private void validatePackagePath(String value, String extension, String fieldName) {
        if (value.startsWith("/") || value.contains("\\") || value.contains("..")) {
            throw new ServiceException(fieldName + "格式不正确");
        }
        if (!value.toLowerCase(Locale.ROOT).endsWith(extension)) {
            throw new ServiceException(fieldName + "必须指向 " + extension + " 文件");
        }
    }

    /**
     * 校验 HTTP(S) 绝对地址（外链平台：应用市场或分发页）
     */
    private void validateHttpUrl(String value) {
        String scheme;
        String host;
        try {
            URI uri = URI.create(value);
            scheme = uri.getScheme();
            host = uri.getHost();
        } catch (IllegalArgumentException e) {
            throw new ServiceException("下载地址必须是 HTTP/HTTPS 绝对地址");
        }
        boolean absoluteHttp = StringUtils.hasText(host)
                && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
        if (!absoluteHttp) {
            throw new ServiceException("下载地址必须是 HTTP/HTTPS 绝对地址");
        }
    }

    /**
     * 获取版本状态
     */
    private String getStatus(String id) {
        LambdaQueryWrapper<SysAppVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysAppVersion::getStatus).eq(SysAppVersion::getId, id);
        SysAppVersion version = sysAppVersionMapper.selectOne(queryWrapper);
        return version != null ? version.getStatus() : null;
    }
}
