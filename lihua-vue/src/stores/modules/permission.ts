import { defineStore } from "pinia";
import type { RouteRecordRaw } from "vue-router";


export const usePermissionStore = defineStore('permission',{
    state: ()=> {
        const sidebarRouters: Array<any> = []
        return {
            sidebarRouters
        }
    },
    actions: {
        initMenu(metaRouterList: Array<any>, staticRoutes: readonly RouteRecordRaw[]): void {
            this.$state.sidebarRouters = init(metaRouterList,staticRoutes)
        }
    },
})

const init = (metaRouterList: Array<any>, staticRoutes: readonly RouteRecordRaw[]) => {
    const staticRouters = generateKey(staticRoutes as Array<any>,'',true)
    return handleOnlyChild(staticRouters).concat(metaRouterList)
}
/**
 * 处理 router/index 中静态路由，生成 key （路由path拼接）
 * @param routers
 */
const generateKey = (routers: Array<any>, key: string, filter: boolean): Array<any> => {
    let menuShowList
    if (filter) {
        menuShowList = routers.filter(route => route.hidden !== true)
    } else {
        menuShowList = routers
    }

    if (menuShowList.length > 0) {
        menuShowList.forEach(menuItem => {
            // 处理path
            menuItem.path = menuItem.path === null ? '' : menuItem.path
            menuItem.path = menuItem.path.startsWith("/") ? menuItem.path.substring(1): menuItem.path
            // 处理双层 / 的情况
            if (key === '/') {
                menuItem.key = '/' + menuItem.path
            } else {
                menuItem.key = key + '/' + menuItem.path
            }

            if (menuItem.children && menuItem.children.length > 0) {
                const child = generateKey(menuItem.children, key, filter)
                menuItem.children = child === null ? [] : child
            } else {
                menuItem.children = null
            }
        })
    }
    return menuShowList
}

/**
 * 处理只有一个子节点的静态路由
 */
const handleOnlyChild = (routers: Array<any>): Array<any> => {
    const array:Array<any> = []
    if (routers) {
        routers.forEach(menu => {
            if (menu.children && menu.children.length === 1) {
                array.push(menu.children[0])
            } else {
                array.push(menu)
            }
        })
    }
    return array
}
