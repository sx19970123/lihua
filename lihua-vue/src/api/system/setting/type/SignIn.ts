export interface SignIn {

    /**
     * 是否启用
     */
    enable?: boolean;

    /**
     * 注册用户拥有的部门id集合
     */
    deptIds?: string[];

    /**
     * 注册用户拥有的默认部门
     */
    defaultDeptId?: string;

    /**
     * 注册用户拥有的岗位id集合
     */
    postIds?: string[];

    /**
     * 注册用户拥有的角色id集合
     */
    roleIds?: string[];

}
