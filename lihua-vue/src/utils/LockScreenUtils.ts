import {useUserStore} from "@/stores/user.ts";
import {decrypt, encrypt} from "@/utils/Crypto.ts";

const LOCK_SCREEN_SETTING_KEY = "lihua_lock_screen_setting_"

const LOCK_KEY = "lihua_lock_screen"

type LockStatus = 'locked' | 'unlocked' | 'logout' | undefined

/**
 * 设置锁屏信息
 * @param autoLock 自动锁定
 * @param timeout 超时时间
 * @param password 密码
 */
export const setLockScreenInfo = (autoLock: boolean, password: string, timeout: number = 0) => {
    localStorage.setItem(LOCK_SCREEN_SETTING_KEY + getUserId(), JSON.stringify({
        autoLock: autoLock,
        timeout: timeout,
        password: encrypt(password)
    }));
}

/**
 * 获取锁屏信息
 */
export const getLockScreenInfo = () => {
    const lockScreenInfo = localStorage.getItem(LOCK_SCREEN_SETTING_KEY + getUserId())
    if (lockScreenInfo) {
        const lockScreen = JSON.parse(lockScreenInfo)
        return {
            autoLock: lockScreen.autoLock,
            timeout: lockScreen.timeout,
            password: decrypt(lockScreen.password)
        }
    }
}

/**
 * 对比密码
 * @param password 密码
 */
export const checkPassword =(password?: string) => {
    const lockScreenInfo = getLockScreenInfo();

    if (lockScreenInfo) {
        return lockScreenInfo.password === password
    }

    return false;
}

/**
 * 锁定
 */
export const screenLock = () => {
    localStorage.setItem(getLockKey(), "locked")
}

/**
 * 解锁
 */
export const screenUnlock = () => {
    localStorage.setItem(getLockKey(), "unlocked")
}

/**
 * 退出登录
 */
export const screenLogout = () => {
    localStorage.setItem(getLockKey(), "logout")
}

/**
 * 获取锁屏状态
 */
export const getLockStatus = (): LockStatus => {
    return localStorage.getItem(getLockKey()) as LockStatus
}

/**
 * 当前已经锁定
 */
export const isLocked = () => {
    return getLockStatus() === "locked"
}

/**
 * 获取锁屏 key
 */
export const getLockKey = () => {
    return LOCK_KEY
}

/**
 * 获取当前登录用户id
 */
const getUserId = (): string => {
    const userStore = useUserStore();
    return  userStore.userId
}