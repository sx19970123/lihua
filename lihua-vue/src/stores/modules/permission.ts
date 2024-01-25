import { defineStore } from "pinia";
import type { RouteRecordRaw } from "vue-router";
import Layout from "@/layout/index.vue"


export const usePermissionStore = defineStore('permission',{
    state: ()=> {
        const sidebarRouters: Array<any> = []
        const viewTabs: Array<any> = []
        return {
            sidebarRouters,
            viewTabs
        }
    },
    actions: {
        initMenu(metaRouterList: Array<any>, staticRoutes: readonly RouteRecordRaw[]): void {
            this.$state.sidebarRouters = init(metaRouterList,staticRoutes)
        },
        initViewTab(starViewVOList: Array<any>, staticRoutes: readonly RouteRecordRaw[]): void {
            // 过滤获取 Layout 为父级的静态路由
            let layoutRoutes =  staticRoutes.filter(route => route.component === Layout)
            // 生成 key
            layoutRoutes = generateKey(layoutRoutes,'',false)
            // 去除父级节点获取子路由组件
            const hasKeyRoutComponentList: Array<any> = []
            getChildren(layoutRoutes,hasKeyRoutComponentList)
            // 根据定义的 viewTabSort 进行排序
            hasKeyRoutComponentList.sort((a, b) => b.meta.viewTabSort - a.meta.viewTabSort)
            // 生成viewTab对象
            if (hasKeyRoutComponentList.length > 0) {
                hasKeyRoutComponentList.forEach(route => {
                    starViewVOList.unshift({
                        label: route.meta.label,
                        icon: route.meta.icon,
                        affix: route.meta.affix,
                        routerPathKey: route.key,
                        star: false,
                        static: true
                    })
                })
            }

            this.$state.viewTabs = starViewVOList
        }
    },
    getters: {

    }
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

/**
 * 获取最底层子节点
 * @param staticRoutes
 * @param arr
 */
const getChildren = (staticRoutes: Array<any>, arr: Array<any>):void => {
    if (staticRoutes) {
        staticRoutes.forEach(route => {
            if (route.children && route.children.length > 0) {
                getChildren(route.children,arr)
            } else {
                arr.push(route)
            }
        })
    }
}
