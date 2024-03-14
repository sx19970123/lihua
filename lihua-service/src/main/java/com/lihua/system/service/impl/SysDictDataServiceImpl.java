package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
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
        if (StringUtils.hasText(dictDataDTO.getDictTypeId())) {
            queryWrapper.lambda().eq(SysDictData::getDictTypeId,dictDataDTO.getDictTypeId());
        }
        // 标签
        if (StringUtils.hasText(dictDataDTO.getLabel())) {
            queryWrapper.lambda().like(SysDictData::getLabel,dictDataDTO.getLabel());
        }
        // 值
        if (StringUtils.hasText(dictDataDTO.getValue())) {
            queryWrapper.lambda().like(SysDictData::getValue,dictDataDTO.getValue());
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
            id = updateById(sysDictData);
        } else {
            id = insert(sysDictData);
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
