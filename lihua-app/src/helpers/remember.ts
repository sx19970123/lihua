import {encrypt, decrypt} from "@/utils/crypto"

const USERNAME_KEY: string = "lihua_username"

const PASSWORD_KEY: string = "lihua_password"

const REMEMBER_ME_KEY: string = "lihua_rememberMe"

const REMEMBER_ME_KEY_TIME: string = "lihua_rememberMeTime"

/**
 * 记住我
 * enable:true 启用，记录账号密码
 * enable:false 关闭，删除已记录内容
 */
export const rememberMe = (enable: boolean, username?: string, password?: string) => {
    // 设置记住我
    if (enable) {
        uni.setStorageSync(REMEMBER_ME_KEY, enable)
        uni.setStorageSync(REMEMBER_ME_KEY_TIME, new Date().getTime())
        uni.setStorageSync(USERNAME_KEY, username)
        uni.setStorageSync(PASSWORD_KEY, encrypt(password || ''))
    } else {
        uni.removeStorageSync(REMEMBER_ME_KEY)
        uni.removeStorageSync(REMEMBER_ME_KEY_TIME)
        uni.removeStorageSync(USERNAME_KEY)
        uni.removeStorageSync(PASSWORD_KEY)
    }
}

/**
 * 获取记住我保存的信息
 * 存在：返回对象
 * 不存在：返回false
 */
export const getRememberedInfo = (): false | {username: string, password: string} => {
    // 从storage中获取记住我设置信息
    const enable = uni.getStorageSync(REMEMBER_ME_KEY)
    if (!enable) {
        return false
    }

    // 拿到记住我时间
    const rememberMeTime = uni.getStorageSync(REMEMBER_ME_KEY_TIME)
    const diffTime = new Date().getTime() - rememberMeTime
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))

    // 相差天数大于30，则删除记住我状态，返回false
    if (diffDays > 30) {
        rememberMe(false)
        return false
    }

    try {
        // 返回保存的账号密码
        const username = uni.getStorageSync(USERNAME_KEY)

        const password = decrypt(uni.getStorageSync(PASSWORD_KEY))

        if (!username || !password) {
            rememberMe(false)
            return false
        }

        return { username, password }

    } catch(err) {
        console.error(err)
        return false
    }
}