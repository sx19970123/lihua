import request from "@/utils/Request.ts";
import type {AuthInfoType} from "@/api/system/auth/type/AuthInfoType.ts";

export const getAuthInfo = () => {
    return request<AuthInfoType>({
        url: '/system/profile/info',
        method: 'get'
    })
}