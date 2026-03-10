package com.lihua.common.utils.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 集合相关工具类
 */
public class CollectionUtils {

    /**
     * 获取集合中的重复元素
     */
    public static <T> Set<T> getRepeatItem(Collection<T> collection) {
        return collection
                .stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

}
