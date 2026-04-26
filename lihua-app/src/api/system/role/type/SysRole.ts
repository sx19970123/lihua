export interface SysRole {
  /**
   * 主键
   */
  id?: string;
  /**
   * 角色名称
   */
  name?: string;
  /**
   * 角色编码
   */
  code?: string;
  /**
   * 角色状态
   */
  status?: string;
  /**
   * 备注
   */
  remark?: string;
  /**
   * 菜单id集合
   */
  menuIds?: string[] | { checked: string[] };
}