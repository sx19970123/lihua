export interface SysDictDataType {
  /**
   * 主键id
   */
  id?: string;

  /**
   * 父级id
   */
  parentId?: string;

  /**
   * 字典类型编码
   */
  dictTypeCode?: string;

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

  /**
   * 状态
   */
  status?: string;

  /**
   * 回显颜色
   */
  tagStyle?: string;

  /**
   * 数据子集
   */
  children?: Array<SysDictDataType>;
}


export interface SysDictDataTypeDTO {
  /**
   * 字典类型编码
   */
  dictTypeCode: string;

  /**
   * 字典标签
   */
  label?: string;

  /**
   * 字典值
   */
  value?: string;

  /**
   * 状态
   */
  status?: string;

  /**
   * 类型
   */
  type?: string;
}
