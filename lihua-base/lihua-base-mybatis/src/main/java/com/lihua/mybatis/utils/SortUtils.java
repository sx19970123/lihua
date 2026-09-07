package com.lihua.mybatis.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.mybatis.model.BaseEntity;
import com.lihua.mybatis.model.SortEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 同级排序归一化工具（占位语义：记录写序号 N 即占据组内第 N 位，原有记录从该位起顺延）
 */
public class SortUtils {

    /**
     * 将同组内全部未删除记录按 (sort 升序, COALESCE(update_time, create_time) 降序, id 降序) 重编为 1..N 的连续序号，
     * 序号有变化的行合并为一条 CASE 批量更新。
     * <p>
     * 并列判定基准取 COALESCE(update_time, create_time)：插入的记录 create_time 最新、编辑的记录 update_time 最新，
     * 两者在并列时均排在既有记录之前，且不受存量数据 update_time 为 NULL 影响。
     * <p>
     * 约定：实体及表的 id/sort/update_time/create_time 列名均为默认驼峰映射，组键列名由调用方以字面量传入。
     *
     * @param groupColumn 组键列名（如 parent_id、dict_type_code）
     * @param groupValue  组键值
     * @return 序号发生变化的行数（0 表示组内本已连续，调用方可据此跳过缓存刷新等后置动作）
     */
    public static <T extends BaseEntity & SortEntity> int normalize(BaseMapper<T> mapper,
                                                                    String groupColumn,
                                                                    Object groupValue) {
        List<T> list = mapper.selectList(new QueryWrapper<T>()
                .eq(groupColumn, groupValue)
                .orderByAsc("sort")
                .orderByDesc("COALESCE(update_time, create_time)")
                .orderByDesc("id"));

        List<Object> changedIds = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        StringBuilder caseSql = new StringBuilder("sort = CASE id ");
        int index = 1;
        for (T item : list) {
            if (!Objects.equals(item.getSort(), index)) {
                changedIds.add(item.getId());
                caseSql.append("WHEN {").append(params.size()).append("} THEN {").append(params.size() + 1).append("} ");
                params.add(item.getId());
                params.add(index);
            }
            index++;
        }

        if (changedIds.isEmpty()) {
            return 0;
        }

        // 批量更新不触碰审计字段（改写 update_time 会污染并列判定基准）
        mapper.update(null, new UpdateWrapper<T>()
                .in("id", changedIds)
                .setSql(caseSql.append("END").toString(), params.toArray()));
        return changedIds.size();
    }
}
