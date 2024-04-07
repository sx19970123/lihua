import { defineStore } from "pinia";
import type {RouterType} from "@/api/system/profile/type/router";


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
    const routers = handleMenuShowFilter(staticRoutes)
    if (routers) {
        return generateKey(routers ,'').concat(metaRouterList)
    }
    return []
}
/**
 * 处理 router/index 中静态路由，生成 key （路由path拼接）
 * @param routers
 */
const generateKey = (routers: any[] , key: string): Array<RouterType> => {
    if (routers.length > 0) {
        routers.forEach(menuItem => {
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
                const child = generateKey(menuItem.children, menuItem.key)
                menuItem.children = child === null ? [] : child
            } else {
                menuItem.children = []
            }
        })
    }
    return routers
}

/**
 * 过滤要在菜单显示的路由
 */
const handleMenuShowFilter = (staticRoutes: any[]) => {
    if (staticRoutes && staticRoutes.length > 0) {
        // 其他静态路由
        const filteredRoutes:any[] = staticRoutes.filter(item => item?.meta?.visible && item.path !== '')
        // 根节点为''并且有子节点的静态路由，当只有一个子节点的visible为true时，在菜单栏只显示页面
        const indexRoutes:any[] = staticRoutes.filter(item => item.path === '' && item.children && item.children.length > 0)
        // 用来保存单个页面的集合
        const singleRoutes:any[] = []

        indexRoutes.forEach(item => {
            const chiVisible = item.children.filter((chi:any) => chi?.meta?.visible)
            if (chiVisible.length === 1) {
                singleRoutes.unshift(chiVisible[0])
            } else {
                singleRoutes.unshift(item)
            }
        })

        if (singleRoutes.length > 0) {
            singleRoutes.forEach(item => {
                filteredRoutes.unshift(item)
            })
        }

        return filteredRoutes;
    }
}
