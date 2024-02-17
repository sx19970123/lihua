import { defineStore } from "pinia";
import type {RouterType} from "@/api/system/user/type/router";


export const usePermissionStore = defineStore('permission',{
    state: ()=> {
        const sidebarRouters: Array<RouterType> = []
        const collapsed: boolean = true
        return {
            sidebarRouters,
            collapsed
        }
    },
    actions: {
        // 加载菜单
        initMenu(metaRouterList: Array<RouterType>, staticRoutes: any[]): void {
            this.$state.sidebarRouters = init(metaRouterList,staticRoutes)
        },
        // 展开菜单
        openCollapsed() {
            this.$state.collapsed = true
        },
        // 折叠菜单
        closeCollapsed() {
            this.$state.collapsed = false
        }
    },
})

const init = (metaRouterList: Array<RouterType>, staticRoutes: any[]): Array<RouterType> => {
    const staticRouters = generateKey(staticRoutes ,'',true)
    return handleOnlyChild(staticRouters).concat(metaRouterList)
}
/**
 * 处理 router/index 中静态路由，生成 key （路由path拼接）
 * @param routers
 */
const generateKey = (routers: any[] , key: string, filter: boolean): Array<RouterType> => {
    let menuShowList: Array<RouterType>
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
                const child = generateKey(menuItem.children, menuItem.key, filter)
                menuItem.children = child === null ? [] : child
            } else {
                menuItem.children = []
            }
        })
    }
    return menuShowList
}

/**
 * 处理只有一个子节点的静态路由
 */
const handleOnlyChild = (routers: Array<RouterType>): Array<RouterType> => {
    const array:Array<RouterType> = []
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
