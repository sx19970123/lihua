import request from "@/utils/request.ts";
import type {SysDept} from "@/api/system/dept/type/sys-dept.ts";
import type {AuthInfoType} from "@/api/system/profile/type/auth-info-type.ts";

/**
 * 保存主题
 */
export const saveTheme = (themeJson: string) => {
    return request({
        url: '/system/profile/theme',
        data: {
          theme: themeJson
        },
        method: 'post'
    })
}

/**
 * 保存基础信息
 */
export const saveBasics = (userInfo: {avatar?: string,nickname?: string,gender?:string,email?:string,phoneNumber?:string}) => {
    return request({
        url: '/system/profile/basics',
        data: userInfo,
        method: 'post'
    })
}

/**
 * 更新密码
 */
export const updatePassword = (oldPassword: string, newPassword: string, confirmPassword: string) => {
    return request<string>({
        url: '/system/profile/password',
        data: {
            oldPassword: oldPassword,
            newPassword: newPassword,
            confirmPassword: confirmPassword
        },
        method: 'post'
    })
}

/**
 * 设置默认部门
 */
export const setDefaultDept = (deptId: string) => {
    return request<SysDept>({
        url: 'system/profile/default/' + deptId,
        method: 'post',
    })
}

/**
 * 获取用户信息
 */
export const queryAuthInfo = () => {
    return request<AuthInfoType>({
        url: '/system/profile/info',
        method: 'get'
    })
}

/**
 * 获取登录后用户数据校验结果
 */
export const queryPostLoginCheckData = () => {
    return request<string[]>({
        url: '/system/profile/postLoginCheck',
        method: 'get',
    })
}