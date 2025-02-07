import request from "@/utils/Request.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";

export const saveTheme = (themeJson: string) => {
    return request({
        url: '/system/profile/theme',
        data: {
          theme: themeJson
        },
        method: 'post'
    })
}

export const saveBasics = (userInfo: {avatar?: string,nickname?: string,gender?:string,email?:string,phoneNumber?:string}) => {
    return request({
        url: '/system/profile/basics',
        data: userInfo,
        method: 'post'
    })
}

export const updatePassword = (oldPassword: string,
                               oldPasswordRequestKey: string,
                               newPassword: string,
                               newPasswordRequestKey: string,
                               confirmPassword: string,
                               confirmPasswordRequestKey: string
                               ) => {
    return request<string>({
        url: '/system/profile/password',
        data: {
            oldPassword: oldPassword,
            oldPasswordRequestKey: oldPasswordRequestKey,
            newPassword: newPassword,
            newPasswordRequestKey: newPasswordRequestKey,
            confirmPassword: confirmPassword,
            confirmPasswordRequestKey: confirmPasswordRequestKey
        },
        method: 'post'
    })
}

/**
 * 设置默认部门
 * @param deptId
 */
export const setDefaultDept = (deptId: string) => {
    return request<SysDept>({
        url: 'system/profile/default/' + deptId,
        method: 'post',
    })
}