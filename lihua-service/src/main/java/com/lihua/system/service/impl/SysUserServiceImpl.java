package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.entity.SysUser;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.system.model.SysUserDTO;
import com.lihua.system.model.SysUserDeptDTO;
import com.lihua.system.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public IPage<SysUserVO> findPage(SysUserDTO sysUserDTO) {
        IPage<SysUserVO> iPage = new Page<>(sysUserDTO.getPageNum(), sysUserDTO.getPageSize());

        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();

        // 部门id
        if (sysUserDTO.getDeptIdList() != null && !sysUserDTO.getDeptIdList().isEmpty()) {
            queryWrapper.in("dept_id", sysUserDTO.getDeptIdList());
        }
        // 昵称
        if (StringUtils.hasText(sysUserDTO.getNickName())) {
            queryWrapper.like("nickname", sysUserDTO.getNickName());
        }
        // 用户名
        if (StringUtils.hasText(sysUserDTO.getUsername())) {
            queryWrapper.like("username", sysUserDTO.getUsername());
        }
        // 电话号码
        if (StringUtils.hasText(sysUserDTO.getPhoneNumber())) {
            queryWrapper.like("phone_number", sysUserDTO.getPhoneNumber());
        }
        // 状态
        if (StringUtils.hasText(sysUserDTO.getStatus())) {
            queryWrapper.eq("status", sysUserDTO.getStatus());
        }
        // 创建时间
        if (sysUserDTO.getCreateTimeList() != null && !sysUserDTO.getCreateTimeList().isEmpty()) {
            queryWrapper.between("create_time", sysUserDTO.getCreateTimeList().get(0),sysUserDTO.getCreateTimeList().get(1));
        }

        queryWrapper.orderByDesc("id");

        iPage = sysUserMapper.findPage(iPage, queryWrapper);

        if (iPage.getRecords().isEmpty()) {
            return iPage;
        }

        // 为用户所属部门赋值
        handleUserDept(iPage.getRecords());

        return iPage;
    }

    @Override
    public SysUserVO findById(String id) {
        SysUserVO sysUserVO = sysUserMapper.findById(id);
        // 设置默认单位id
        if (!sysUserVO.getDefaultDeptIdList().isEmpty()) {
            List<String> list = sysUserVO.getDefaultDeptIdList().stream().filter(StringUtils::hasText).toList();
            sysUserVO.setDefaultDeptId(!list.isEmpty() ? list.get(0) : null);
        }
        return sysUserVO;
    }

    @Override
    public String save(SysUserDTO sysUserDTO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDTO, sysUser);
        String id = null;
        if (!StringUtils.hasText(sysUserDTO.getId())) {
            id = insert(sysUser);
        } else {
            id = update(sysUser);
        }

        // 保存部门数据

        // 保存岗位数据

        // 保存角色数据

        return id;
    }

    @Override
    public void deleteByIds(List<String> ids) {
        checkStatus(ids);
        sysUserMapper.deleteBatchIds(ids);
    }

    // 处理用户所属部门
    private void handleUserDept(List<SysUserVO> records) {
        List<String> userIds = records.stream().map(SysUserVO::getId).toList();
        List<SysUserDeptDTO> userDeptByUserIds = sysUserMapper.findUserDeptByUserIds(userIds);
        Map<String, List<SysUserDeptDTO>> groupByUserId = userDeptByUserIds.stream().collect(Collectors.groupingBy(SysUserDeptDTO::getUserId));

        // 为用户所属部门赋值
        records.forEach(user -> {
            groupByUserId.forEach((key, value) -> {
                if (user.getId().equals(key)) {
                    user.setDeptLabelList(value.stream().map(SysUserDeptDTO::getDeptName).toList());
                }
            });
        });
    }

    // 新增用户
    private String insert(SysUser sysUser) {
        return sysUser.getId();
    }

    // 更细用户信息
    private String update(SysUser sysUser) {
        return sysUser.getId();
    }

    // 检查状态
    private void checkStatus(List<String> ids) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
                .in(SysUser::getId,ids)
                .eq(SysUser::getStatus,"0");
        Long count = sysUserMapper.selectCount(queryWrapper);

        if (count > 0) {
            throw new ServiceException("存在状态为正常的用户，无法删除");
        }
    }
}
