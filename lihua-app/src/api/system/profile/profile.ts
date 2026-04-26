import request from "@/utils/request";
import type {SysDept} from "@/api/system/dept/type/sys-dept";
import type {passwordType} from "@/api/system/profile/type/password-type";
import type { AuthInfoType } from "@/api/system/profile/type/auth-info-type";

// 获取用户信息
export const queryAuthInfo = () => {
    return request<AuthInfoType>({
        url: 'app/system/profile/info',
        method: 'GET'
    })
}

/**
 * 保存基础信息
 */
export const saveBasics = (userInfo: {avatar?: string,nickname?: string,gender?:string,email?:string,phoneNumber?:string}) => {
    return request({
        url: 'app/system/profile/basics',
        data: userInfo,
        method: 'POST'
    })
}

/**
 * 修改密码
 */
export const updatePassword = (password: passwordType) => {
    return request<string>({
        url: 'app/system/profile/password',
        data: password,
        method: 'POST'
    })
}

/**
 * 设置默认部门
 */
export const setDefaultDept = (deptId: string) => {
    return request<SysDept>({
        url: 'app/system/profile/default/' + deptId,
        method: 'POST',
    })
}

/**
 * 用户注销
 */
export const accountDeactivate = () => {
	return request<string>({
		url: 'app/system/profile/deactivate',
		method: "DELETE"
	})
}

/**
 * 检查密码
 */
export const checkPassword = (password: string) => {
	return request<boolean>({
		url: 'app/system/profile/checkPassword',
		method: "POST",
		data: {
			password
		}
	})
}