package com.lihua.common.enums;

/**
 * 字典枚举统一契约：实现类的 value 与字典种子对齐。
 * value（码）归代码所有（枚举与种子一致，字典 UI 不可随意改码）；
 * label/tagStyle/sort 归字典 UI 所有（运行时可维护）
 */
public interface DictEnum {

    /**
     * 字典类型编码（如 sys_status）
     */
    String getType();

    /**
     * 字典项值（与种子 value 对齐）
     */
    String getValue();
}
