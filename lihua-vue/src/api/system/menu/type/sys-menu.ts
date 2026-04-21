export interface SysMenu {
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
   * page类型数据从父级到子节点的路由地址拼接
   */
  routerPathKey?: string;
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
   * 页面缓存(0缓存、1不缓存)
   */
  cache?: string;
  /**
   * 外链类型地址
   */
  linkPath?: string;

  /**
   * 链接打开方式
   */
  linkOpenType?: string;

  /**
   * 排序
   */
  sort?: number;

  /**
   * 跳过 view-tab 管理
   */
  viewTab?: string;

  /**
   * 路由参数
   */
  query?: string;

  /**
   * 子元素
   */
  children?: Array<SysMenuVO>
}

export interface SysMenuVO extends SysMenu{
  statusIsNormal?: boolean,
  updateStatusLoading?: boolean
}