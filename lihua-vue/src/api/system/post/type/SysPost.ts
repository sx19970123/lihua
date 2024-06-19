export interface SysPost {
    /**
     * 主键
     */
    id?: string;

    /**
     * 部门主键
     */
    deptId?: string;

    /**
     * 部门编码
     */
    deptCode?: string;

    /**
     * 岗位名称
     */
    name?: string;

    /**
     * 岗位编码
     */
    code?: string;

    /**
     * 岗位排序
     */
    sort?: number;

    /**
     * 岗位状态
     */
    status?: string;

    /**
     * 岗位负责人姓名
     */
    manager?: string;

    /**
     * 岗位联系电话
     */
    phoneNumber?: string;

    /**
     * 岗位邮件
     */
    email?: string;

    /**
     * 岗位传真号码
     */
    fax?: string;

    /**
     * 备注
     */
    remark?: string;

    /**
     * 删除标识
     */
    delFlag?: string;
}

export interface SysPostVO extends SysPost{
    /**
     * 所属部门名称
     */
    deptName?: string

    statusIsNormal?: boolean

    updateStatusLoading?: boolean
}

export interface SysPostDTO {

    /**
     * 岗位状态
     */
    status?: string;

    /**
     * 岗位名称
     */
    name?: string;

    /**
     * 岗位编码
     */
    code?: string;

    /**
     * 所属部门id
     */
    deptId?: string | null;

    /**
     * 当前页数
     */
    pageNum: number;

    /**
     * 每页记录数
     */
    pageSize: number;
}