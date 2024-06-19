import type{ Dayjs } from "dayjs";

/**
 * 表示一个 SysDictType 实体。
 * 此接口定义了 SysDictType 对象的结构。
 */
export interface SysDictType {
  /**
   * SysDictType 的主键 id。
   */
  id?: string;

  /**
   * 字典类型名称。
   */
  name?: string;

  /**
   * 字典类型编码。
   */
  code?: string;

  /**
   * 表示字典类型是否为树结构。
   */
  type?: string;

  /**
   * 关于字典类型的额外备注或注释。
   */
  remark?: string;

  /**
   * 字典类型的逻辑删除标识。
   */
  delFlag?: string;

  /**
   * 创建人。
   */
  createId?: string;

  /**
   * 创建时间。
   */
  createTime?: string;

  /**
   * 更新人。
   */
  updateId?: string;

  /**
   * 更新时间。
   */
  updateTime?: string;

  /**
   * 字典类型状态
   */
  status?: string;
}

export interface SysDictTypeVO extends SysDictType {

  statusIsNormal?: boolean

  updateStatusLoading?: boolean
}

export interface SysDictTypeDTO {

  /**
   * 字典类型名称。
   */
  name?: string;

  /**
   * 字典类型编码。
   */
  code?: string;

  /**
   * 字典类型状态
   */
  status?: string;

  /**
   * 开始结束时间
   */
  startEndTime?: [Dayjs?, Dayjs?];

  /**
   * 当前页数
   */
  pageNum: number;

  /**
   * 每页记录数
   */
  pageSize: number;
}
