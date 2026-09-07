package com.lihua.mybatis.model;

/**
 * 支持同级排序归一化的实体契约：id/sort 属性名与列名遵循默认驼峰映射（配合 SortUtils 使用）
 */
public interface SortEntity {

    String getId();

    Integer getSort();
}
