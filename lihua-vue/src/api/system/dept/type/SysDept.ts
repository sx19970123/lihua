import type {SysPost} from "@/api/system/post/type/SysPost.ts";

export interface SysDept {
  /**
   * 主键id
   */
  id?: string;
  /**
   * 父级id
   */
  parentId?: string;
  /**
   * 部门/岗位名称
   */
  name?: string;
  /**
   * 部门/岗位编码
   */
  code?: string;
  /**
   * 数据状态
   */
  status?: string;
  /**
   * 数据排序
   */
  sort?: number;
  /**
   * 负责人
   */
  manager?: string;
  /**
   * 联系电话
   */
  phoneNumber?: string;
  /**
   * 邮箱
   */
  email?: string;
  /**
   * 传真
   */
  fax?: string;
  /**
   * 备注
   */
  remark?: string;
  /**
   * 子节点
   */
  children?: SysDeptVO[];
  /**
   * 部门信息
   */
  sysPostList?: SysPost[];

  value?: string
}

export interface SysDeptVO extends SysDept {

  statusIsNormal?: boolean

  updateStatusLoading?: boolean
}