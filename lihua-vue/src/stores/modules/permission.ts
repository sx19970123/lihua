import { defineStore } from "pinia";
import type { RouteRecordRaw } from "vue-router";


export const usePermissionStore = defineStore('permission',{
    state: ()=> {
        const sidebarRouters: Array<any> = []
        const viewTabsRouters: Array<any> = []
        return {
            sidebarRouters,
            viewTabsRouters
        }
    },
    actions: {
        initMenu(metaRouterList: Array<any>, staticRoutes: readonly RouteRecordRaw[]):void {
            this.$state.sidebarRouters = init(metaRouterList,staticRoutes)
        }
    },
    getters: {

    }
})

const init = (metaRouterList: Array<any>, staticRoutes: readonly RouteRecordRaw[]) => {
    const staticRouters =  filterMenuRouter(staticRoutes as Array<any>,"")
    return handleOnlyChild(staticRouters).concat(metaRouterList)
}
/**
 * 处理 router/index 中静态路由，hidden属性为true的不进行列表展示，并指定key属性
 * @param routers
 */
const filterMenuRouter = (routers: Array<any> ,key: string): Array<any> => {
    const menuShowList = routers.filter(route => route.hidden !== true)
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
                const child = filterMenuRouter(menuItem.children ,key)
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

/**
 * 将 path 组合为 key 供菜单使用
 * @param allMenu
 * @param key
 */
const handleMenuKey = (allMenu: Array<any>,key: string): Array<any> => {
    if (allMenu && allMenu.length > 0) {
        allMenu.forEach(menu => {
            // 处理path
            menu.path = menu.path === null ? '' : menu.path
            menu.path = menu.path.startsWith("/") ? menu.path.substring(1): menu.path
            // 处理双层 / 的情况
            if (key === '/') {
                menu.key = '/' + menu.path
            } else {
                menu.key = key + '/' + menu.path
            }
            if (menu.children && menu.children.length > 0) {
                handleMenuKey(menu.children,menu.key)
            } else {
                menu.children = null
            }
        })
    }
    return allMenu;
}
