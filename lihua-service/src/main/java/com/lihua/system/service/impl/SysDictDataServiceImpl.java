package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.system.entity.SysDictData;
import com.lihua.system.mapper.SysDictDataMapper;
import com.lihua.system.model.SysDictDataDTO;
import com.lihua.system.service.SysDictDataService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class SysDictDataServiceImpl implements SysDictDataService {

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public IPage<SysDictData> findPage(SysDictDataDTO dictDataDTO) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        IPage<SysDictData> ipage = new Page<>(dictDataDTO.getPageNum(), dictDataDTO.getPageSize());
        sysDictDataMapper.selectPage(ipage,queryWrapper);
        return ipage;
    }

    @Override
    public SysDictData findById(String id) {
        Object cacheObject = redisCache.getCacheObject(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + id);
        if (cacheObject == null) {
            return sysDictDataMapper.selectById(id);
        }

        return (SysDictData) cacheObject;
    }

    @Override
    public String save(SysDictData sysDictData) {
        String id;
        if (StringUtils.hasText(sysDictData.getId())) {
            id = insert(sysDictData);
        } else {
            id = updateById(sysDictData);
        }
        // 更新缓存
        redisCache.setCacheObject(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + sysDictData.getId(),sysDictData);
        return id;
    }

    @Override
    public void deleteByIds(List<String> ids) {
        sysDictDataMapper.deleteBatchIds(ids);
        ids.forEach(id -> redisCache.delete(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + id));
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

}
