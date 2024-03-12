interface SysDictDataType {
  /**
   * 主键id
   */
  id?: string;

  /**
   * 父级id
   */
  parentId?: string;

  /**
   * 字典类型id
   */
  dictTypeId?: string;

  /**
   * 字典标签
   */
  label?: string;

  /**
   * 字典值
   */
  value?: string;

  /**
   * 字典排序
   */
  sort?: number;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 删除标识
   */
  delFlag?: string;
}


interface SysDictDataQueryType {
  /**
   * 字典类型id
   */
  dictTypeId: string;

  /**
   * 字典标签
   */
  label?: string;

  /**
   * 字典值
   */
  value?: string;
}
