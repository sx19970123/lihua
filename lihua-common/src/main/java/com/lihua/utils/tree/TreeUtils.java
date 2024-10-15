package com.lihua.utils.tree;

import com.lihua.utils.string.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 通用构建树方法
 */
public class TreeUtils {

    private static final String DEFAULT_ID = "Id";
    private static final String DEFAULT_PARENT_ID = "ParentId";
    private static final String DEFAULT_CHILDREN = "Children";


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
        return buildTree(list,DEFAULT_ID,DEFAULT_PARENT_ID,DEFAULT_CHILDREN);
    }

    public static <T> List<T> buildTree(List<T> list, String propKeyName,String propParentKeyName,String propChildrenName) {
        final String finalPropKeyName = StringUtils.initialUpperCase(propKeyName);
        final String finalPropParentKeyName = StringUtils.initialUpperCase(propParentKeyName);
        final String finalPropChildrenName = StringUtils.initialUpperCase(propChildrenName);

        // 获取全部 key（id）集合
        List<String> ids = list
                .stream()
                .map(item -> Objects.requireNonNull(get(item, finalPropKeyName)).toString())
                .toList();

        List<T> respList = new ArrayList<>();

        // 过滤出 parentKey（pid）不在 ids 集合中的数据，为根节点
        List<String> parentIds = list
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
            List<T> build = build(list, finalPropKeyName, finalPropParentKeyName, finalPropChildrenName, parentId);
            respList.addAll(build);
        });

        return respList;
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
     * 通过反射调用 get 方法，返回对象
     */
    private static <T,K> K get(T item , String prop) {
        Class<?> cls = item.getClass();
        for (Method method : cls.getMethods()) {
            if (method.getName().equals("get" + prop)) {
                try {
                    return (K) method.invoke(item);
                } catch (Exception e) {
                    throw new RuntimeException("TreeUtils 通过反射调用get方法发生异常");
                }
            }
        }
        return null;
    }

    /**
     * 通过反射调用 get 方法，返回集合
     */
    private static <T,K> List<K> getList(T item , String prop) {
        Class<?> cls = item.getClass();
        for (Method method : cls.getMethods()) {
            if (method.getName().equals("get" + prop)) {
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
        }
        return null;
    }


    /**
     * 通过反射调用set 方法
     */
    private static <T,V> void set(T item , String prop , V value) {
        Class<?> cls = item.getClass();
        for (Method method : cls.getMethods()) {
            if (method.getName().equals("set" + prop)) {
                try {
                    method.invoke(item, value);
                } catch (Exception e) {
                    throw new RuntimeException("TreeUtils 通过反射调用set方法发生异常");
                }
            }
        }
    }
}
