package com.lihua.utils.tree;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lihua.exception.ServiceException;
import com.lihua.utils.json.JsonUtils;
import com.lihua.utils.string.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 通用构建树方法
 */
@Slf4j
public class TreeUtils {
    private static final String DEFAULT_ID = "Id";
    private static final String DEFAULT_PARENT_ID = "ParentId";
    private static final String DEFAULT_CHILDREN = "Children";

    // Set方法缓存
    private static final Cache<Class<?>, Method> SET_METHOD_CACHE = CacheBuilder.newBuilder().maximumSize(600).build();
    // Get方法缓存
    private static final Cache<Class<?>, Map<String,Method>> GET_METHOD_CACHE = CacheBuilder.newBuilder().maximumSize(1800).build();

    /**
     * 通过lambda表达式构建
     */
    public static LambdaTreeUtils lambda() {
        return new LambdaTreeUtils();
    }

    /**
     * 将 List<T> 构建为 树形结构 ，默认属性为 id parentId children
     * 可通过重载指定属性
     */
    public static <T> List<T> buildTree(List<T> list) {
        return buildTree(list, DEFAULT_ID, DEFAULT_PARENT_ID, DEFAULT_CHILDREN);
    }

    public static <T> List<T> buildTree(List<T> list, String propKeyName,String propParentKeyName,String propChildrenName) {
        List<T> originCollection = new ArrayList<>(list);
        final String finalPropKeyName = StringUtils.initialUpperCase(propKeyName);
        final String finalPropParentKeyName = StringUtils.initialUpperCase(propParentKeyName);
        final String finalPropChildrenName = StringUtils.initialUpperCase(propChildrenName);

        // 获取全部 key（id）集合
        List<String> ids = originCollection
                .stream()
                .map(item -> Objects.requireNonNull(get(item, finalPropKeyName)).toString())
                .toList();

        List<T> respList = new ArrayList<>();

        // 过滤出 parentKey（pid）不在 ids 集合中的数据，为根节点
        List<String> parentIds = originCollection
                .stream()
                .filter(item -> {
                    String parentId = get(item, finalPropParentKeyName);
                    return org.springframework.util.StringUtils.hasText(parentId) && !ids.contains(parentId);
                })
                .map(item -> Objects.requireNonNull((String)get(item, finalPropParentKeyName)))
                .distinct()
                .toList();

        // 当条件筛选时，根节点不一定全部都是相同的，所以根据不同的跟节点分别执行树的构建，最后将所有树都放到同一集合中
        parentIds.forEach(parentId -> {
            List<T> build = build(originCollection, finalPropKeyName, finalPropParentKeyName, finalPropChildrenName, parentId);
            respList.addAll(build);
        });

        return respList;
    }

    /**
     * 将树形集合 List<T> 扁平化，去除树形结构，默认子节点属性为 children
     */
    public static <T> List<T> flattenTree(List<T> list) {
        return flattenTree(list, DEFAULT_CHILDREN);
    }

    public static <T> List<T> flattenTree(List<T> list, String propChildrenName) {
        if (list == null || list.isEmpty()) {
            return list;
        }

        List<T> flattenTree = new ArrayList<>();
        flatten(list, flattenTree, StringUtils.initialUpperCase(propChildrenName));
        return flattenTree;
    }

    /**
     *
     * @param list 可组成树形结构的集合
     * @param propKeyName id 属性名
     * @param propParentKeyName pid 属性名
     * @param propChildrenName children 属性名
     * @param rootParentValue root 节点 pid的值
     * @return 树形结构集合
     */
    private static <T,K,V> List<T> build(List<T> list,
                                          String propKeyName,
                                          String propParentKeyName,
                                          String propChildrenName,
                                          V rootParentValue) {

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        Map<K, List<T>> map = new HashMap<>();
        List<T> treeList = new ArrayList<>();

        // 将节点分散到哈希表
        list.forEach(item -> map.computeIfAbsent(get(item,propParentKeyName), k -> new ArrayList<>()).add(item));

        // 构建树形结构
        // 对集合使用 removeIf ，当符合条件节点使用完毕后从当前删除。这样同一集合中多棵树中，需要递归的次数会越来越少
        list.removeIf(item -> {
            List<T> children = map.get(get(item, propKeyName));
            if (children != null) {
                List<T> child = getList(item, propChildrenName);
                if (child == null) {
                    set(item,propChildrenName,new ArrayList<>());
                    child = getList(item, propChildrenName);
                    if (child != null) {
                        child.addAll(children);
                    }
                }
            }

            // 返回true 即删除集合中item 元素
            if (Objects.equals(get(item, propParentKeyName), rootParentValue)) {
                treeList.add(item);
                return true;
            } else {
                return false;
            }
        });

        return treeList;
    }

    /**
     * 将树形结构扁平化
     * @param list 树形结构集合
     * @param flattenTree 扁平化后的元素集合
     * @param propChildrenName 子节点属性名称
     * @param <T> 集合类型
     */
    private static <T> void flatten(List<T> list, List<T> flattenTree, String propChildrenName) {
        list.forEach(item -> {
            // 深拷贝item对象，防止修改原数据
            T copyItem = JsonUtils.deepCopy(item);
            // 清空item中的children属性
            set(copyItem, propChildrenName, new ArrayList<>());
            // 向扁平化的集合中添加元素
            flattenTree.add(copyItem);

            // 继续递归获取每个对象
            List<T> itemChildren = getList(item, propChildrenName);
            if (itemChildren != null && !itemChildren.isEmpty()) {
                flatten(itemChildren, flattenTree, propChildrenName);
            }
        });
    }

    /**
     * 通过反射调用 get 方法，返回对象
     */
    private static <T,K> K get(T item, String prop) {
        Method method = getGetMethod(item, prop);
        try {
            return (K) method.invoke(item);
        } catch (Exception e) {
            throw new RuntimeException("TreeUtils 通过反射调用get方法发生异常");
        }
    }

    /**
     * 通过反射调用 get 方法，返回集合
     */
    @SneakyThrows
    private static <T,K> List<K> getList(T item, String prop) {
        Method method = getGetMethod(item, prop);
        Object invoke;
        try {
            invoke = method.invoke(item);
            if (invoke != null) {
                return (List<K>) invoke;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("TreeUtils 通过反射调用get方法发生异常");
        }
    }


    /**
     * 通过反射调用set方法
     */
    private static <T,V> void set(T item, String prop, V value) {
        Method method = getSetMethod(item, prop);
        try {
            method.invoke(item, value);
        } catch (Exception e) {
            throw new ServiceException("TreeUtils 通过反射调用set方法发生异常");
        }
    }

    /**
     * 从缓存中获取目标set方法
     */
    private static <T> Method getSetMethod(T item, String prop) {
        Class<?> cls = item.getClass();
        try {
            return SET_METHOD_CACHE.get(cls, () -> Arrays.stream(cls.getMethods()).filter(mtd -> mtd.getName().equals("set" + prop)).findFirst().get());
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("TreeUtils 通过SET_METHOD_CACHE获取set方法发生异常");
        }
    }

    /**
     * 从缓存中获取目标get方法
     */
    private static <T> Method getGetMethod(T item, String prop) {
        Class<?> cls = item.getClass();
        Map<String, Method> methodMap;
        try {
            methodMap = GET_METHOD_CACHE.get(cls, () -> {
                Map<String, Method> map = new HashMap<>();
                map.put(prop, Arrays.stream(cls.getMethods()).filter(mtd -> mtd.getName().equals("get" + prop)).findFirst().get());
                return map;
            });
        } catch (ExecutionException e) {
            throw new ServiceException("TreeUtils 通过SET_METHOD_CACHE获取get方法发生异常");
        }

        if (methodMap.get(prop) == null) {
            methodMap.put(prop, Arrays.stream(cls.getMethods()).filter(mtd -> mtd.getName().equals("get" + prop)).findFirst().get());
            GET_METHOD_CACHE.put(cls, methodMap);
        }

        return methodMap.get(prop);
    }
}
