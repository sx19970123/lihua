import { useUserStore } from "@/stores/modules/user.ts";
import { useThemeStore } from "@/stores/modules/theme.ts";

let userStore: ReturnType<typeof useUserStore> | null = null;
let themeStore: ReturnType<typeof useThemeStore> | null = null;

/**
 * 判断是否拥有指定的角色
 * @param routeRoleList 静态路由指定的角色编码集合
 */
export const hasRouteRole = (routeRoleList?: string[]): boolean => {
    // 静态路由没有指定均为所有用户可访问
    if (!routeRoleList || routeRoleList.length === 0) {
        return true;
    }

    // 初始化赋值 userStore
    if (!userStore) {
        userStore = useUserStore();
    }

    // 获取用户角色编码
    const userRoleList = userStore.roleCodes;

    // 返回用户是否拥有静态路由指定角色
    return routeRoleList.some(role => userRoleList.includes(role));
};

/**
 * 返回当前用户是否为 admin
 */
export const isAdmin = () => {
    return hasRouteRole(["ROLE_admin"]);
}


/**
 * 导航菜单是否为分组模式
 */
export const isSiderGroup = (): 'group' | undefined => {
    if (!themeStore) {
        themeStore = useThemeStore();
    }
    return themeStore.siderGroup ? 'group' : undefined
}