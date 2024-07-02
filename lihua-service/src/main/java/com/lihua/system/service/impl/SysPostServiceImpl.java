package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.exception.ServiceException;
import com.lihua.system.entity.SysPost;
import com.lihua.system.mapper.SysPostMapper;
import com.lihua.system.model.dto.SysPostDTO;
import com.lihua.system.model.vo.SysPostVO;
import com.lihua.system.service.SysPostService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysPostServiceImpl implements SysPostService {

    @Resource
    private SysPostMapper sysPostMapper;

    @Override
    public IPage<SysPostVO> findPage(SysPostDTO dto) {
        IPage<SysPostVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());

        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();

        // 岗位名称
        if (StringUtils.hasText(dto.getName())) {
            queryWrapper.like("sys_post.name",dto.getName());
        }
        // 岗位编码
        if (StringUtils.hasText(dto.getCode())) {
            queryWrapper.like("sys_post.code",dto.getCode());
        }
        // 岗位状态
        if (StringUtils.hasText(dto.getStatus())) {
            queryWrapper.like("sys_post.status",dto.getStatus());
        }
        // 所属单位
        if (StringUtils.hasText(dto.getDeptId())) {
            queryWrapper.like("sys_post.dept_id",dto.getDeptId());
        }
        queryWrapper.eq("sys_post.del_flag", "0");
        queryWrapper
                .lambda()
                .orderByAsc(SysPost::getSort);

        sysPostMapper.findPage(iPage,queryWrapper);

        return iPage;
    }

    @Override
    public SysPost findById(String id) {
        return sysPostMapper.selectById(id);
    }

    @Override
    public String save(SysPost sysPost) {
        checkPostCode(sysPost);
        if (!StringUtils.hasText(sysPost.getId())) {
            return insert(sysPost);
        }
        return update(sysPost);
    }

    private String insert(SysPost sysPost) {
        sysPost.setCreateTime(LocalDateTime.now());
        sysPost.setCreateId(LoginUserContext.getUserId());
        sysPost.setDelFlag("0");
        sysPostMapper.insert(sysPost);
        return sysPost.getId();
    }

    private String update(SysPost sysPost) {
        sysPost.setUpdateId(LoginUserContext.getUserId());
        sysPost.setUpdateTime(LocalDateTime.now());
        sysPostMapper.updateById(sysPost);
        return sysPost.getId();
    }

    @Override
    public void deleteByIds(List<String> ids) {
        // 数据是否为停用状态
        checkStatus(ids);
        // 数据下没有分配用户
        checkUser(ids);
        sysPostMapper.deleteBatchIds(ids);
    }

    private void checkStatus(List<String> ids) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysPost::getStatus,"0")
                .in(SysPost::getId,ids);
        Long count = sysPostMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("岗位状态为正常不允许删除");
        }
    }

    private void checkUser(List<String> ids) {
        Long count = sysPostMapper.postUserCount(ids);
        if (count > 0) {
            throw new ServiceException("存在用户不允许删除");
        }
    }

    @Override
    public List<SysPost> findPostByDeptId(List<String> deptIdList) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(SysPost::getDeptId,deptIdList)
                .orderByAsc(SysPost::getSort);
        return sysPostMapper.selectList(queryWrapper);
    }

    @Override
    public Long findCountByDeptId(List<String> deptIdList) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysPost::getDeptId,deptIdList);
        return sysPostMapper.selectCount(queryWrapper);
    }

    @Override
    public Map<String, List<SysPost>> getPostOptionByDeptId(List<String> deptIds) {
        QueryWrapper<SysPost> deptQueryWrapper = new QueryWrapper<>();
        deptQueryWrapper.lambda()
                .in(SysPost::getDeptId, deptIds)
                .eq(SysPost::getStatus,"0")
                .orderByAsc(SysPost::getSort);
        List<SysPost> sysPosts = sysPostMapper.selectList(deptQueryWrapper);

        return sysPosts.stream().collect(Collectors.groupingBy(SysPost::getDeptId));
    }

    @Override
    public String updateStatus(String id, String currentStatus) {
        UpdateWrapper<SysPost> updateWrapper = new UpdateWrapper<>();
        String status = "0".equals(currentStatus) ? "1" : "0";

        updateWrapper.lambda()
                .set(SysPost::getStatus, status)
                .set(SysPost::getUpdateId, LoginUserContext.getUserId())
                .set(SysPost::getUpdateTime, LocalDateTime.now())
                .eq(SysPost::getId, id);
        sysPostMapper.update(null, updateWrapper);
        return status;
    }

    private void checkPostCode(SysPost sysPost) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(SysPost::getCode, sysPost.getCode());
        List<SysPost> sysPosts = sysPostMapper.selectList(queryWrapper);

        if (sysPosts.isEmpty()) {
            return;
        }

        if (sysPosts.size() > 1) {
            throw new ServiceException("岗位编码已存在");
        }

        if (!sysPosts.get(0).getId().equals(sysPost.getId())) {
            throw new ServiceException("岗位编码已存在");
        }
    }
}
