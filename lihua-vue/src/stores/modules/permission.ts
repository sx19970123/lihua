import { defineStore } from "pinia";
import router from "@/router";
import type { RouteRecordRaw } from "vue-router";
import type { Component } from "vue";
import Layout from "@/layout/index.vue";
import MiddleView from "@/components/MiddleView/index.vue"

const modules = import.meta.glob("./../../views/**/*.vue")

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
        initRouters(routerList:any):void {
            const data = initMenuList(routerList)
            // 初始化菜单信息
            this.$state.sidebarRouters = data
            console.log(data)
            // 初始化动态路由
            initDynamicRouter(routerList)
            console.log(router.options)

        }
    },
    getters: {

    }
})

/**
 * 初始化侧边菜单，整合 @router 下定义路由 和数据库中存储的动态路由
 * @param routerList
 */
const initMenuList = (routerList:Array<any>):Array<any> => {
    // 处理静态路由
    const handleStaticRouter = (): Array<any> => {
        const menuRouter = filterMenuRouter(router.options.routes)
        const hasKeyMenuData = handleMenuKey(menuRouter,'')
        return handleOnlyChild(hasKeyMenuData)
    }
    // 处理菜单，统一与静态路由的格式
    const handleMenu = (): Array<any>  => {
        const menuRouter = refactorMenu(routerList)
        return handleMenuKey(menuRouter,'')
    }
    return handleStaticRouter().concat(handleMenu())
}

/**
 * 处理 router/index 下 item.meta.menuShow 为 true 的路由，整合到菜单进行显示
 * @param routers
 */
const filterMenuRouter = (routers: Readonly<RouteRecordRaw[]>): Array<any> => {
    const menuShowList = routers.filter(item => item.meta && item.meta.menuShow)
    if (menuShowList.length > 0) {
        menuShowList.forEach(menuItem => {
            if (menuItem.children && menuItem.children.length > 0) {
                const child = filterMenuRouter(menuItem.children)
                menuItem.children = child === null ? [] : child
            }
        })
    }
    return menuShowList
}

const handleOnlyChild = (hasKeyMenuData: Array<any>): Array<any> => {
    const array:Array<any> = []
    if (hasKeyMenuData) {
        hasKeyMenuData.forEach(menu => {
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
 * 处理将 menu 数据转为 Router
 * @param routerList
 */
const refactorMenu = (routerList: Array<any>): Array<any> => {
    const arrayMenu: Array<MenuType> = []
    interface MetaType {
        noCache?: boolean,
        label?: string,
        title?: string,
        icon?: string | Component,
        affix?: boolean,
        menuShow?: boolean
    }
    interface MenuType {
        path: string | null,
        name: string | null,
        meta: MetaType | null,
        component: Component | null,
        children: Array<MenuType> | null
    }

     if (routerList.length > 0) {
         routerList.forEach(item => {
             let menu: MenuType = {
                 path: null,
                 meta: null,
                 name: null,
                 component: null,
                 children: []
             }
             let meta: MetaType = {}
             meta.label = item.label
             meta.icon = item.icon
             meta.menuShow = true
             meta.noCache = item.noCache

             menu.meta = meta
             menu.path = item.routerPath
             menu.name = item.routerPath.replace("/","").charAt(0).toUpperCase() + item.routerPath.slice(1)

             // 页面
             if (item.menuType === 'page') {
                 for (let path in modules) {
                     const dir = path.split('views/')[1]
                     if (dir === item.componentPath) {
                         console.log('dir==',dir)
                         menu.component = () => modules[path]()
                     }
                 }

             }
             // 菜单
             else {
                 menu.children = refactorMenu(item.children)
                 // 顶级菜单
                 if (item.children && item.parentId === '0') {
                     menu.component = () => Layout
                 }
                 // 中间层级菜单
                 else if (item.children && item.parentId !== '0') {
                     menu.component = () => MiddleView
                 }
             }
             arrayMenu.push(menu)
         })
     }
     return arrayMenu;
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

const initDynamicRouter = (routerList: Array<any>) => {
    const routers = refactorMenu(routerList)
    routers.forEach(route => {
        router.addRoute(route)
    })
}
