package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.system.entity.SysNotice;
import com.lihua.system.entity.SysUserNotice;
import com.lihua.system.mapper.SysNoticeMapper;
import com.lihua.system.model.dto.SysNoticeDTO;
import com.lihua.system.model.vo.SysNoticeVO;
import com.lihua.system.service.SysNoticeService;
import com.lihua.system.service.SysUserNoticeService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        saveUserNotice(id, sysNoticeDTO.getUserIdList());
        return id;
    }

    @Override
    public String release(String id) {
        return "";
    }

    @Override
    public String revoke(String id) {
        return "";
    }

    @Override
    public void deleteByIds(String ids) {

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
        LocalDateTime now = LocalDateTime.now();
        String createId = LoginUserContext.getUserId();
        sysUserNoticeService.deleteByUserIds(id);
        userIdList.forEach(userId -> {
            SysUserNotice sysUserNotice = new SysUserNotice(userId, id, "0", "0", null, now, createId);
            sysUserNotices.add(sysUserNotice);
        });
        sysUserNoticeService.save(sysUserNotices);
    }
}
