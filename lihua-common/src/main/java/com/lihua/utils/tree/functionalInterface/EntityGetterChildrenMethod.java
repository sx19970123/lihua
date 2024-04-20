package com.lihua.utils.tree.functionalInterface;

import java.util.List;

@FunctionalInterface
public interface EntityGetterChildrenMethod<T> {
    List<T> apply(T t);
}
