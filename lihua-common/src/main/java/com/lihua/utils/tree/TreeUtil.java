package com.lihua.utils.tree;

import com.lihua.utils.string.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 通用构建树方法，大数据量性能不佳，慎用
 */
public class TreeUtil {

    private static final String DEFAULT_ID = "Id";
    private static final String DEFAULT_PARENT_ID = "ParentId";
    private static final String DEFAULT_CHILDREN = "Children";
    private static final String DEFAULT_ROOT_VALUE = "0";

    /**
     * 将 List<T> 构建为 树形结构 ，默认属性为 id parentId children 0
     * 可通过重载指定属性
     * @param list
     * @return
     * @param <T>
     */
    public static <T> List<T> buildTree(List<T> list) {
        return buildTree(list,DEFAULT_ID,DEFAULT_PARENT_ID,DEFAULT_CHILDREN,DEFAULT_ROOT_VALUE);
    }


    public static <T,V> List<T> buildTree(List<T> list,V rootValue) {
        return buildTree(list,DEFAULT_ID,DEFAULT_PARENT_ID,DEFAULT_CHILDREN,rootValue);
    }
    public static <T,V> List<T> buildTree(List<T> list, String propKeyName,String propParentKeyName) {
        propKeyName = StringUtils.initialUpperCase(propKeyName);
        propParentKeyName = StringUtils.initialUpperCase(propParentKeyName);
        return build(list,propKeyName,propParentKeyName,DEFAULT_CHILDREN,DEFAULT_ROOT_VALUE);
    }

    public static <T,V> List<T> buildTree(List<T> list, String propKeyName,String propParentKeyName , V rootValue) {
        propKeyName = StringUtils.initialUpperCase(propKeyName);
        propParentKeyName = StringUtils.initialUpperCase(propParentKeyName);
        return build(list,propKeyName,propParentKeyName,DEFAULT_CHILDREN,rootValue);
    }

    public static <T,V> List<T> buildTree(List<T> list, String propKeyName,String propParentKeyName,String propChildrenName , V rootValue) {
        propKeyName = StringUtils.initialUpperCase(propKeyName);
        propParentKeyName = StringUtils.initialUpperCase(propParentKeyName);
        propChildrenName = StringUtils.initialUpperCase(propChildrenName);
        return build(list,propKeyName,propParentKeyName,propChildrenName,rootValue);
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
            return null;
        }

        Map<K, List<T>> map = new HashMap<>();
        List<T> treeList = new ArrayList<>();

        // 将节点分散到哈希表
        list.forEach(item -> map.computeIfAbsent(get(item,propParentKeyName), k -> new ArrayList<>()).add(item));

        // 构建树形结构
        for (T item : list) {
            List<T> children = map.get(get(item, propKeyName));
            if (children != null) {
                List<Object> child = getList(item, propChildrenName);
                child.addAll(children);
            }

            if (Objects.equals(get(item, propParentKeyName), rootParentValue)) {
                treeList.add(item);
            }
        }

        return treeList;
    }


    /**
     * 通过反射调用 get 方法，返回对象
     */
    public static <T,K> K get(T item , String prop) {
        Class<?> cls = item.getClass();
        for (Method method : cls.getMethods()) {
            if (method.getName().equals("get" + prop)) {
                try {
                    return (K) method.invoke(item);
                } catch (Exception e) {
                    throw new RuntimeException("TreeUtil 通过反射调用get方法发生异常");
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
                Object invoke = null;
                try {
                    invoke = method.invoke(item);
                    if (invoke == null) {
                        set(item,prop,new ArrayList<>());
                        return (List<K>) method.invoke(item);
                    }
                     return  (List<K>) invoke;
                } catch (Exception e) {
                    throw new RuntimeException("TreeUtil 通过反射调用get方法发生异常");
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
                    throw new RuntimeException("TreeUtil 通过反射调用set方法发生异常");
                }
            }
        }
    }
}
