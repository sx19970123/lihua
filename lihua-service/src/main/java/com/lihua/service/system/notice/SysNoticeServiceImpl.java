package com.lihua.service.system.notice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.enums.ServerSentEventsEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.sse.ServerSentEventsResult;
import com.lihua.entity.system.SysNotice;
import com.lihua.entity.system.SysUserNotice;
import com.lihua.mapper.system.SysNoticeMapper;
import com.lihua.model.system.dto.SysNoticeDTO;
import com.lihua.model.system.vo.SysNoticeVO;
import com.lihua.model.system.vo.SysUserNoticeVO;
import com.lihua.service.system.user.SysUserNoticeService;
import com.lihua.service.system.user.SysUserService;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.sse.ServerSentEventsManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    @Resource
    private SysNoticeMapper sysNoticeMapper;

    @Resource
    private SysUserNoticeService sysUserNoticeService;

    @Resource
    private SysUserService sysUserService;

    @Resource(name = "applicationTaskExecutor")
    private Executor executor;

    @Override
    public IPage<SysNotice> queryPage(SysNoticeDTO sysNoticeDTO) {
        IPage<SysNotice> iPage = new Page<>(sysNoticeDTO.getPageNum(), sysNoticeDTO.getPageSize());
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();

        // 查询字段
        queryWrapper.lambda().select(
                SysNotice::getId,
                SysNotice::getTitle,
                SysNotice::getType,
                SysNotice::getStatus,
                SysNotice::getRemark,
                SysNotice::getCreateTime,
                SysNotice::getPriority,
                SysNotice::getUserScope
                );

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
    public SysNoticeVO queryById(String id) {
        // 查询 SysNotice 表
        SysNotice sysNotice = sysNoticeMapper.selectById(id);
        SysNoticeVO sysNoticeVO = new SysNoticeVO();
        BeanUtils.copyProperties(sysNotice, sysNoticeVO);

        // 用户范围为全部的情况下，直接返回
        if ("0".equals(sysNotice.getUserScope())) {
            return sysNoticeVO;
        }
        // 指定用户情况下，查询关联用户后返回
        List<String> userIds = sysUserNoticeService.queryUserIds(id);
        sysNoticeVO.setUserIdList(userIds);
        return sysNoticeVO;
    }

    @Override
    public SysNoticeVO preview(String id) {
        return sysNoticeMapper.preview(id);
    }

    @Override
    @Transactional
    public String save(SysNoticeDTO sysNoticeDTO) {
        if ("1".equals(sysNoticeDTO.getUserScope()) &&
            (sysNoticeDTO.getUserIdList() == null || sysNoticeDTO.getUserIdList().isEmpty())) {
            throw new ServiceException("请指定接收用户");
        }

        sysNoticeDTO.setStatus("0");
        String id;
        if (!StringUtils.hasText(sysNoticeDTO.getId())) {
            id = insert(sysNoticeDTO);
        } else {
            id = update(sysNoticeDTO);
        }
        // 指定用户范围时，保存关联表
        if ("1".equals(sysNoticeDTO.getUserScope())) {
            // 删除所有关联关系
            sysUserNoticeService.deleteByNoticeIds(Collections.singletonList(id));
            saveUserNotice(id, sysNoticeDTO.getUserIdList());
        }

        return id;
    }

    @Override
    @Transactional
    public String release(String id) {
        String status = getStatus(id);
        if ("1".equals(status)) {
            throw new ServiceException("该消息通知已发布");
        }
        // 更新状态等信息
        UpdateWrapper<SysNotice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SysNotice::getId, id)
                        .set(SysNotice::getStatus, "1")
                        .set(SysNotice::getReleaseTime, DateUtils.now())
                        .set(SysNotice::getReleaseId, LoginUserContext.getUserId());
        sysNoticeMapper.update(updateWrapper);

        // 获取发送的消息 id title
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysNotice::getId, id)
                .select(SysNotice::getId, SysNotice::getTitle, SysNotice::getType, SysNotice::getUserScope);
        SysNotice sysNotice = sysNoticeMapper.selectOne(queryWrapper);

        if ("0".equals(sysNotice.getUserScope())) {
            // 发送范围为全部用户时，删除所有关联关系批量插入
            sysUserNoticeService.deleteByNoticeIds(Collections.singletonList(id));
            saveUserNotice(id, sysUserService.queryAllUserIds());
        } else {
            // 发送范围为指定用户时，重置star和read状态
            sysUserNoticeService.resetStatus(id);
        }

        // 新线程发送消息
        executor.execute(() -> {
            // 获取被推送的用户id
            List<String> userIds = sysUserNoticeService.queryUserIds(id);
            // sse 推送数据
            ServerSentEventsManager.send(userIds, new ServerSentEventsResult<>(ServerSentEventsEnum.SSE_NOTICE, sysNotice));
        });

        return id;
    }

    @Override
    @Transactional
    public String revoke(String id) {
        String status = getStatus(id);
        if (!"1".equals(status)) {
            throw new ServiceException("仅发布状态消息通知可撤销");
        }

        // 更新状态等信息
        UpdateWrapper<SysNotice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SysNotice::getId, id)
                .set(SysNotice::getStatus, "2")
                .set(SysNotice::getUpdateTime, DateUtils.now())
                .set(SysNotice::getUpdateId, LoginUserContext.getUserId());
        sysNoticeMapper.update(updateWrapper);
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
            throw new ServiceException("已发布消息通知无法删除");
        }

        sysNoticeMapper.deleteByIds(ids);
        // 删除用户关联表数据
        sysUserNoticeService.deleteByNoticeIds(ids);
    }

    @Override
    public IPage<SysUserNoticeVO> queryListByUserId(String userId, SysNoticeDTO sysNoticeDTO) {
        IPage<SysUserNoticeVO> iPage = new Page<>(sysNoticeDTO.getPageNum(), sysNoticeDTO.getPageSize());
        QueryWrapper<SysUserNoticeVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sys_user_notice.user_id", userId)
                        .eq("sys_notice.status", "1")
                        .eq("sys_notice.del_flag", "0")
                        .orderByDesc("sys_notice.release_time");
        // 是否查询star
        if ("1".equals(sysNoticeDTO.getStar())) {
            queryWrapper.eq("sys_user_notice.star_flag", sysNoticeDTO.getStar());
        }
        return sysNoticeMapper.queryListByUserId(iPage, queryWrapper);
    }

    /**
     * 新增数据
     */
    private String insert(SysNoticeDTO sysNoticeDTO) {
        SysNotice sysNotice = new SysNotice();
        BeanUtils.copyProperties(sysNoticeDTO, sysNotice);
        sysNotice.setDelFlag("0");
        sysNotice.setCreateId(LoginUserContext.getUserId());
        sysNotice.setCreateTime(DateUtils.now());
        sysNoticeMapper.insert(sysNotice);
        return sysNotice.getId();
    }

    /**
     * 修改数据
     */
    private String update(SysNoticeDTO sysNoticeDTO) {
        String status = getStatus(sysNoticeDTO.getId());
        if ("1".equals(status)) {
            throw new ServiceException("已发布消息通知无法编辑");
        }

        SysNotice sysNotice = new SysNotice();
        BeanUtils.copyProperties(sysNoticeDTO, sysNotice);
        sysNotice.setUpdateId(LoginUserContext.getUserId());
        sysNotice.setUpdateTime(DateUtils.now());
        sysNoticeMapper.updateById(sysNotice);
        return sysNotice.getId();
    }

    /**
     * 保存关联表数据
     */
    private void saveUserNotice(String id, List<String> userIdList) {
        List<SysUserNotice> sysUserNotices = new ArrayList<>();

        if (userIdList == null || userIdList.isEmpty()) {
            throw new ServiceException("指定用户不存在");
        }

        userIdList.forEach(userId -> {
            SysUserNotice sysUserNotice = new SysUserNotice(userId, id, "0", "0", null);
            sysUserNotices.add(sysUserNotice);
        });

        // 批量保存
        sysUserNoticeService.save(sysUserNotices);
    }

    /**
     * 获取通知公告状态
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
