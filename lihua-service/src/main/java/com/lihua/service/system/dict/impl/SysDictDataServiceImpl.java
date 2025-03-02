package com.lihua.service.system.dict.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.cache.RedisCache;
import com.lihua.exception.ServiceException;
import com.lihua.model.dict.SysDictDataVO;
import com.lihua.entity.system.SysDictData;
import com.lihua.mapper.system.SysDictDataMapper;
import com.lihua.model.system.dto.SysDictDataDTO;
import com.lihua.service.system.dict.SysDictDataService;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.dict.DictUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.tree.TreeUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysDictDataServiceImpl implements SysDictDataService {

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public List<SysDictData> queryList(SysDictDataDTO dictDataDTO) {
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
            return TreeUtils.buildTree(sysDictData);
        }

        return sysDictData;
    }

    @Override
    public List<SysDictDataVO> queryDictOptionList(String dictTypeCode) {
        List<SysDictDataVO> dictData = DictUtils.getDictData(dictTypeCode);
        return TreeUtils.buildTree(dictData);
    }

    @Override
    public Map<String, List<SysDictDataVO>> queryDictOptionList(List<String> dictTypeCodeList) {
        Map<String, List<SysDictDataVO>> map = new HashMap<>();
        dictTypeCodeList.forEach(dictTypeCode -> {
            List<SysDictDataVO> dictOptionList = queryDictOptionList(dictTypeCode);
            map.put(dictTypeCode, dictOptionList);
        });

        return map;
    }

    @Override
    public String save(SysDictData sysDictData) {
        // 系统状态不允许修改
        checkSysStatus(sysDictData);
        // 检查字典值是否重复
        checkValue(sysDictData);
        // 检查字典名称是否重复
        checkLabel(sysDictData);

        String id;
        if (StringUtils.hasText(sysDictData.getId())) {
            id = updateById(sysDictData);
        } else {
            id = insert(sysDictData);
        }
        // 删除缓存
        DictUtils.removeDictCache(sysDictData.getDictTypeCode());
        return id;
    }

    @Override
    public void updateDataTypeCode(String oldTypeCode, String newTypeCode) {
        UpdateWrapper<SysDictData> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .lambda()
                .set(SysDictData::getDictTypeCode,newTypeCode)
                .set(SysDictData::getUpdateId,LoginUserContext.getUserId())
                .set(SysDictData::getUpdateTime, DateUtils.now())
                .eq(SysDictData::getDictTypeCode,oldTypeCode);
        sysDictDataMapper.update(updateWrapper);
    }

    @Override
    public List<String> typeIdsToDataIds(List<String> ids) {
        return sysDictDataMapper.selectDataIdsByTypeIds(ids);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        checkChildren(ids);
        checkStatus(ids);

        // 删除数据
        sysDictDataMapper.deleteByIds(ids);

        // 查询删除数据对应的type code
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(SysDictData::getId,ids)
                .select(SysDictData::getDictTypeCode);
        List<SysDictData> sysDictData = sysDictDataMapper.selectList(queryWrapper);

        // 删除缓存
        if (!sysDictData.isEmpty()) {
            sysDictData
                .stream()
                .map(SysDictData::getDictTypeCode)
                .distinct()
                .forEach(DictUtils::removeDictCache);
        }
    }

    private String insert(SysDictData sysDictData) {
        sysDictData.setCreateId(LoginUserContext.getUserId());
        sysDictData.setDelFlag("0");
        sysDictData.setCreateTime(DateUtils.now());
        sysDictDataMapper.insert(sysDictData);
        return sysDictData.getId();
    }

    private String updateById(SysDictData sysDictData) {
        sysDictData.setUpdateId(LoginUserContext.getUserId());
        sysDictData.setUpdateTime(DateUtils.now());
        sysDictDataMapper.updateById(sysDictData);
        return sysDictData.getId();
    }

    private void checkChildren(List<String> ids) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .in(SysDictData::getParentId,ids)
                .eq(SysDictData::getDelFlag,"0");
        Long count = sysDictDataMapper.selectCount(queryWrapper);
        if (count != 0) {
            throw new ServiceException("存在子集不允许删除");
        }
    }

    private void checkStatus(List<String> ids) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysDictData::getId,ids)
                .eq(SysDictData::getStatus,"0")
                .eq(SysDictData::getDelFlag,"0");
        Long count = sysDictDataMapper.selectCount(queryWrapper);
        if (count != 0) {
            throw new ServiceException("字典数据状态正常不允许删除");
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

    private void checkLabel(SysDictData sysDictData) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysDictData::getLabel,sysDictData.getLabel())
                .eq(SysDictData::getDictTypeCode,sysDictData.getDictTypeCode());
        List<SysDictData> sysDictDataList = sysDictDataMapper.selectList(queryWrapper);
        if (!sysDictDataList.isEmpty()) {
            if (sysDictDataList.size() > 1) {
                throw new ServiceException("当前字典标签已存在");
            } else {
                if (!sysDictDataList.get(0).getId().equals(sysDictData.getId())) {
                    throw new ServiceException("当前字典标签已存在");
                }
            }
        }
    }

    // 系统状态标签不允许修改
    private void checkSysStatus(SysDictData sysDictData) {
        if ("sys_status".equals(sysDictData.getDictTypeCode())) {
            throw new ServiceException("系统状态不允许修改");
        }
    }
}
