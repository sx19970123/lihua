package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.ServiceException;
import com.lihua.system.entity.SysDictData;
import com.lihua.system.mapper.SysDictDataMapper;
import com.lihua.system.model.SysDictDataDTO;
import com.lihua.system.service.SysDictDataService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.tree.TreeUtil;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class SysDictDataServiceImpl implements SysDictDataService {

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private RedisCache redisCache;

    @Override
    public List<SysDictData> findList(SysDictDataDTO dictDataDTO) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        // 类型id
        if (StringUtils.hasText(dictDataDTO.getDictTypeCode())) {
            queryWrapper.lambda().eq(SysDictData::getDictTypeCode,dictDataDTO.getDictTypeCode());
        }
        // 标签
        if (StringUtils.hasText(dictDataDTO.getLabel())) {
            queryWrapper.lambda().like(SysDictData::getLabel,dictDataDTO.getLabel());
        }
        // 值
        if (StringUtils.hasText(dictDataDTO.getValue())) {
            queryWrapper.lambda().like(SysDictData::getValue,dictDataDTO.getValue());
        }
        // 状态
        if (StringUtils.hasText(dictDataDTO.getStatus())) {
            queryWrapper.lambda().eq(SysDictData::getStatus,dictDataDTO.getStatus());
        }
        // 排序
        queryWrapper.lambda().orderByAsc(SysDictData::getSort);
        List<SysDictData> sysDictData = sysDictDataMapper.selectList(queryWrapper);

        // 树形结构需要构建子数据
        if ("1".equals(dictDataDTO.getType())) {
            return TreeUtil.buildTree(sysDictData);
        }

        return sysDictData;
    }

    @Override
    public List<SysDictData> findDictOptionList(String dictTypeCode) {
        List<SysDictData> cacheList = redisCache.getCacheList(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode);

        if (cacheList.isEmpty()) {
            return new ArrayList<>();
        }

        return cacheList;
    }

    @Override
    public String save(SysDictData sysDictData) {
        // 检查字典值是否重复
        checkValue(sysDictData);

        String id;
        if (StringUtils.hasText(sysDictData.getId())) {
            id = updateById(sysDictData);
        } else {
            id = insert(sysDictData);
        }
        // 更新缓存
        resetCache(sysDictData.getDictTypeCode());
        return id;
    }

    @Override
    public void deleteByIds(List<String> ids) {
        checkChildren(ids);

        // 删除数据库数据前先刷新redis 缓存
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(SysDictData::getId,ids)
                .select(SysDictData::getDictTypeCode);
        List<SysDictData> sysDictData = sysDictDataMapper.selectList(queryWrapper);
        if (!sysDictData.isEmpty()) {
            sysDictData.forEach(data -> {
                resetCache(data.getDictTypeCode());
            });
        }

        // 删除数据
        sysDictDataMapper.deleteBatchIds(ids);
    }

    @Override
    public void resetCache(String dictTypeCode) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysDictData::getDictTypeCode,dictTypeCode)
                .eq(SysDictData::getStatus,"0")
                .eq(SysDictData::getDelFlag,"0");

        List<SysDictData> activityList = sysDictDataMapper.selectList(queryWrapper);

        // 数据为空时删除redis 缓存
        if (activityList.isEmpty()) {
            redisCache.delete(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode);
        } else {
            redisCache.setCacheList(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode,activityList);
        }
    }

    private String insert(SysDictData sysDictData) {
        sysDictData.setCreateId(LoginUserContext.getUserId());
        sysDictData.setCreateTime(LocalDateTime.now());
        sysDictDataMapper.insert(sysDictData);
        return sysDictData.getId();
    }

    private String updateById(SysDictData sysDictData) {
        sysDictData.setUpdateId(LoginUserContext.getUserId());
        sysDictData.setUpdateTime(LocalDateTime.now());
        sysDictDataMapper.updateById(sysDictData);
        return sysDictData.getId();
    }

    private void checkChildren(List<String> ids) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysDictData::getParentId,ids);
        Long count = sysDictDataMapper.selectCount(queryWrapper);
        if (count != 0) {
            throw new ServiceException("存在子集不允许删除");
        }
    }

    private void checkValue(SysDictData sysDictData) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysDictData::getValue,sysDictData.getValue())
                .eq(SysDictData::getDictTypeCode,sysDictData.getDictTypeCode());
        List<SysDictData> sysDictDataList = sysDictDataMapper.selectList(queryWrapper);
        if (!sysDictDataList.isEmpty()) {
            if (sysDictDataList.size() > 1) {
                throw new ServiceException("当前字典值已存在");
            } else {
                if (!sysDictDataList.get(0).getId().equals(sysDictData.getId())) {
                    throw new ServiceException("当前字典值已存在");
                }
            }
        }
    }
}
