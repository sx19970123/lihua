package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.enums.ServerSentEventsEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.sse.ServerSentEventsResult;
import com.lihua.system.entity.SysNotice;
import com.lihua.system.entity.SysUserNotice;
import com.lihua.system.mapper.SysNoticeMapper;
import com.lihua.system.model.dto.SysNoticeDTO;
import com.lihua.system.model.vo.SysNoticeVO;
import com.lihua.system.service.SysNoticeService;
import com.lihua.system.service.SysUserNoticeService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.sse.ServerSentEventsManager;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    @Resource
    private SysNoticeMapper sysNoticeMapper;

    @Resource
    private SysUserNoticeService sysUserNoticeService;

    @Override
    public IPage<SysNotice> findPage(SysNoticeDTO sysNoticeDTO) {
        IPage<SysNotice> iPage = new Page<>(sysNoticeDTO.getPageNum(), sysNoticeDTO.getPageSize());
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();

        // 查询字段
        queryWrapper.lambda().select(
                SysNotice::getId,
                SysNotice::getTitle,
                SysNotice::getType,
                SysNotice::getStatus,
                SysNotice::getRemark,
                SysNotice::getCreateTime);

        // 标题
        if (StringUtils.hasText(sysNoticeDTO.getTitle())) {
            queryWrapper.lambda().like(SysNotice::getTitle, sysNoticeDTO.getTitle());
        }

        // 状态
        if (StringUtils.hasText(sysNoticeDTO.getStatus())) {
            queryWrapper.lambda().eq(SysNotice::getStatus, sysNoticeDTO.getStatus());
        }

        // 类型
        if (StringUtils.hasText(sysNoticeDTO.getType())) {
            queryWrapper.lambda().eq(SysNotice::getType, sysNoticeDTO.getType());
        }

        // 排序
        queryWrapper.lambda().orderByDesc(SysNotice::getCreateTime);

        sysNoticeMapper.selectPage(iPage, queryWrapper);

        return iPage;
    }

    @Override
    public SysNoticeVO findById(String id) {
        return sysNoticeMapper.findById(id);
    }

    @Override
    @Transactional
    public String save(SysNoticeDTO sysNoticeDTO) {
        sysNoticeDTO.setStatus("0");
        String id;
        if (!StringUtils.hasText(sysNoticeDTO.getId())) {
            id = insert(sysNoticeDTO);
        } else {
            id = update(sysNoticeDTO);
        }
        // 保存关联表
        saveUserNotice(id, sysNoticeDTO.getUserIdList());
        return id;
    }

    @Override
    public String release(String id) {
        String status = getStatus(id);
        if (!"0".equals(status)) {
            throw new ServiceException("仅未发布状态数据可发布");
        }
        // 更新状态等信息
        UpdateWrapper<SysNotice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SysNotice::getId, id)
                        .set(SysNotice::getStatus, "1")
                        .set(SysNotice::getReleaseTime, LocalDateTime.now())
                        .set(SysNotice::getUpdateTime, LocalDateTime.now())
                        .set(SysNotice::getUpdateId, LoginUserContext.getUserId());
        sysNoticeMapper.update(updateWrapper);

        // 获取发送的消息 id title
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysNotice::getId, id)
                .select(SysNotice::getId, SysNotice::getTitle);
        SysNotice sysNotice = sysNoticeMapper.selectOne(queryWrapper);
        // 获取被推送的用户id
        List<String> userIds = sysUserNoticeService.findUserIds(id);
        // sse 推送数据
        ServerSentEventsManager.send(userIds, new ServerSentEventsResult<SysNotice>(ServerSentEventsEnum.SSE_NOTICE, sysNotice));
        return id;
    }

    @Override
    @Transactional
    public String revoke(String id) {
        String status = getStatus(id);
        if (!"1".equals(status)) {
            throw new ServiceException("仅发布状态数据可撤销");
        }

        // 更新状态等信息
        UpdateWrapper<SysNotice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SysNotice::getId, id)
                .set(SysNotice::getStatus, "0")
                .set(SysNotice::getUpdateTime, LocalDateTime.now())
                .set(SysNotice::getUpdateId, LoginUserContext.getUserId());
        sysNoticeMapper.update(updateWrapper);
        // 删除用户关联表数据
        sysUserNoticeService.deleteByNoticeIds(Collections.singletonList(id));
        return id;
    }

    @Override
    @Transactional
    public void deleteByIds(List<String> ids) {
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysNotice::getStatus, "1")
                .in(SysNotice::getId, ids);

        Long count = sysNoticeMapper.selectCount(queryWrapper);

        if (count > 0) {
            throw new ServiceException("已发布数据无法删除");
        }

        sysNoticeMapper.deleteBatchIds(ids);
        // 删除用户关联表数据
        sysUserNoticeService.deleteByNoticeIds(ids);
    }

    /**
     * 新增数据
     * @param sysNoticeDTO
     * @return
     */
    private String insert(SysNoticeDTO sysNoticeDTO) {
        SysNotice sysNotice = new SysNotice();
        BeanUtils.copyProperties(sysNoticeDTO, sysNotice);
        sysNotice.setDelFlag("0");
        sysNotice.setCreateId(LoginUserContext.getUserId());
        sysNotice.setCreateTime(LocalDateTime.now());
        sysNoticeMapper.insert(sysNotice);
        return sysNotice.getId();
    }

    /**
     * 修改数据
     * @param sysNoticeDTO
     * @return
     */
    private String update(SysNoticeDTO sysNoticeDTO) {
        SysNotice sysNotice = new SysNotice();
        BeanUtils.copyProperties(sysNoticeDTO, sysNotice);
        sysNotice.setUpdateId(LoginUserContext.getUserId());
        sysNotice.setUpdateTime(LocalDateTime.now());
        sysNoticeMapper.updateById(sysNotice);
        return sysNotice.getId();
    }

    /**
     * 保存关联表数据
     * @param id
     * @param userIdList
     */
    private void saveUserNotice(String id, List<String> userIdList) {
        List<SysUserNotice> sysUserNotices = new ArrayList<>();
        sysUserNoticeService.deleteByNoticeIds(Collections.singletonList(id));
        userIdList.forEach(userId -> {
            SysUserNotice sysUserNotice = new SysUserNotice(userId, id, "0", "0", null);
            sysUserNotices.add(sysUserNotice);
        });
        sysUserNoticeService.save(sysUserNotices);
    }

    /**
     * 获取通知公告状态
     * @param id
     * @return
     */
    private String getStatus(String id) {
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(SysNotice::getStatus)
                .eq(SysNotice::getId, id);

        SysNotice sysNotice = sysNoticeMapper.selectOne(queryWrapper);
        if (sysNotice != null) {
            return sysNotice.getStatus();
        }

        return null;
    }
}
