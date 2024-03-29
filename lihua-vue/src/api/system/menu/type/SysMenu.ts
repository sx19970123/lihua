interface SysMenu {
  /**
   * 主键
   */
  id?: string;
  /**
   * 父级菜单id
   */
  parentId?: string;
  /**
   * 菜单名称
   */
  label?: string;
  /**
   * 鼠标悬浮内容展示
   */
  title?: string;
  /**
   * 菜单/页面
   */
  menuType?: string;
  /**
   * 路由地址
   */
  routerPath?: string;
  /**
   * 组建路径
   */
  componentPath?: string;
  /**
   * 是否显示（0显示、1隐藏）
   */
  visible?: string;
  /**
   * 菜单状态(0正常、1停用)
   */
  status?: string;
  /**
   * 权限标识符
   */
  perms?: string;
  /**
   * 菜单图标
   */
  icon?: string;
  /**
   * 逻辑删除标志
   */
  delFlag?: string;
  /**
   * 备注
   */
  remark?: string;
  /**
   * 不进行页面缓存(0缓存、1不缓存)
   */
  noCache?: string;
  /**
   * 外链类型地址
   */
  linkPath?: string;

  /**
   * 子元素
   */
  children?: Array<SysMenu>
}
