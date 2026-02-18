package com.lihua.utils.tree.functionalInterface;

import java.util.List;
@FunctionalInterface
public interface EntitySetterChildrenMethod <T> {
    void apply(T t, List<T> list);
}
