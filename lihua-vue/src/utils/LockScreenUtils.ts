import {useUserStore} from "@/stores/user.ts";
import {decrypt, encrypt} from "@/utils/Crypto.ts";

const LOCK_SCREEN_KEY = "lihua_lock_screen_"

/**
 * 设置锁屏信息
 * @param autoLock 自动锁定
 * @param timeout 超时时间
 * @param password 密码
 */
export const setLockScreenInfo = (autoLock: boolean, password: string, timeout: number = 0) => {
    const userStore = useUserStore();

    localStorage.setItem(LOCK_SCREEN_KEY + userStore.userId, JSON.stringify({
        autoLock: autoLock,
        timeout: timeout,
        password: encrypt(password)
    }));
}

/**
 * 获取锁屏信息
 */
export const getLockScreenInfo = () => {
    const userStore = useUserStore();
    const lockScreenInfo = localStorage.getItem(LOCK_SCREEN_KEY + userStore.userId)
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