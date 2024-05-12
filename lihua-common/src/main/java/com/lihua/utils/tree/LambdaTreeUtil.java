package com.lihua.utils.tree;

import com.lihua.utils.tree.functionalInterface.EntityGetterChildrenMethod;
import com.lihua.utils.tree.functionalInterface.EntityGetterStringMethod;
import com.lihua.utils.tree.functionalInterface.EntitySetterChildrenMethod;
import org.springframework.util.StringUtils;
import java.util.*;

public class LambdaTreeUtil{
    /**
     * 构建树
     * @param originCollection 待处理的扁平化树集合
     * @param getKeyMethod 获取树 id 方法
     * @param getParentKeyMethod 获取树 parentId 方法
     * @param getChildrenMethod 获取树 children 方法
     * @return 构建完成的树集合
     * @param <T> 业务类
     */
    public <T> List<T> buildTree(List<T> originCollection,
                                    EntityGetterStringMethod<T> getKeyMethod,
                                    EntityGetterStringMethod<T> getParentKeyMethod,
                                    EntityGetterChildrenMethod<T> getChildrenMethod,
                                    EntitySetterChildrenMethod<T> setChildrenMethod) {
        List<T> respTreeList = new ArrayList<>();
        // 获取全部根节点id
        List<String> parentKeyList = getParentKeyList(originCollection, getKeyMethod, getParentKeyMethod);
        parentKeyList.forEach(parentKey -> {
            respTreeList.addAll(build(originCollection,getKeyMethod,getParentKeyMethod,getChildrenMethod,setChildrenMethod,parentKey));
            System.out.println(originCollection.size());
        });
        return respTreeList;
    }

    // 获取全部父节点id
    private <T> List<String> getParentKeyList(Collection<T> originCollection,
                                             EntityGetterStringMethod<T> getKeyMethod,
                                             EntityGetterStringMethod<T> getParentKeyMethod) {
        // 获取全部id
        List<String> idList = originCollection.stream().map(getKeyMethod::apply).toList();
        // 根据id获取根节点id
        return originCollection.stream()
                .filter(item -> StringUtils.hasText(getParentKeyMethod.apply(item)) && !idList.contains(getParentKeyMethod.apply(item)))
                .map(getParentKeyMethod::apply)
                .distinct()
                .toList();
    }

    /**
     * 构建树型结构
     * @param originCollection 待处理的扁平化树集合
     * @param getKeyMethod 获取树 id 方法
     * @param getParentKeyMethod 获取树 parentId 方法
     * @param getChildrenMethod 获取树 children 方法
     * @param rootParentValue root节点id
     * @return 构建完成的树集合
     * @param <T> 业务类
     */
    private <T> List<T> build(Collection<T> originCollection,
                              EntityGetterStringMethod<T> getKeyMethod,
                              EntityGetterStringMethod<T> getParentKeyMethod,
                              EntityGetterChildrenMethod<T> getChildrenMethod,
                              EntitySetterChildrenMethod<T> setterChildrenMethod,
                              String rootParentValue) {
        Map<String, List<T>> map = new HashMap<>();
        List<T> treeList = new ArrayList<>();
        // 将节点分散到哈希表，key 为 节点 父id
        originCollection.forEach(item -> map.computeIfAbsent(getParentKeyMethod.apply(item), k -> new ArrayList<>()).add(item));

        // 构建树形结构
        // 对集合使用 removeIf ，当符合条件节点使用完毕后从当前删除。这样同一集合中多棵树中，需要递归的次数会越来越少
        originCollection.removeIf(item -> {
            List<T> children = map.get(getKeyMethod.apply(item));
            if (children != null) {
                List<T> child = getChildrenMethod.apply(item);
                if (child == null) {
                    setterChildrenMethod.apply(item,new ArrayList<>());
                    child = getChildrenMethod.apply(item);
                    child.addAll(children);
                }
            }

            // 返回true 即删除集合中item 元素
            if (Objects.equals(getParentKeyMethod.apply(item), rootParentValue)) {
                treeList.add(item);
                return true;
            } else {
                return false;
            }
        });

        return treeList;
    }
}
