import { defineStore } from "pinia";
import type {RouteRecordRaw} from "vue-router";
import router from "@/router";
import Layout from "@/layout/index.vue";
import IFrame from "@/components/iframe/index.vue";
import MiddleView from "@/components/middle-view/index.vue";
import type {RouterType} from "@/api/system/auth/type/AuthInfoType.ts";
import { h } from "vue";
import Icon from "@/components/icon/index.vue";
import type {ItemType} from "ant-design-vue";
import {hasRouteRole} from "@/utils/Auth.ts"
import {isEqual} from "lodash-es"
import {useThemeStore} from "@/stores/theme.ts";
let themeStore: ReturnType<typeof useThemeStore> | null = null;

// 获取 views 下的所有 vue 组件
const modules = import.meta.glob("../views/**/*.vue")

export const usePermissionStore = defineStore('permission',{
    state: ()=> {
        const menuRouters: Array<ItemType> = []
        const collapsed: boolean = true
        return {
            menuRouters,
            collapsed
        }
    },
    actions: {
        // 初始化动态路由
        initDynamicRouter(metaRouterList: Array<RouterType>) {
            // 处理各个层级 Component
            handleRouterComponent(metaRouterList)
            // 顶级无父组件目录、页面添加layout父级
            metaRouterList.forEach((route, index) => {
                const isRoot= route.children === null || route.children.length === 0
                const isPageType = route.type === 'page';
                const isMenuType = route.type === 'directory';
                const isLinkType = route.type === 'link';
                if ((isPageType || isLinkType || isMenuType) && isRoot) {
                    const parentRoute: RouteRecordRaw  = {
                        children: [route as any],
                        path: "",
                        component: Layout,
                        name: route.name? route.name + index: 'Parent' + route.id
                    };
                    router.addRoute(parentRoute);
                } else {
                    router.addRoute(route as any);
                }
            });
        },
        // 加载菜单
        initMenu(metaRouterList: Array<RouterType>, staticRoutes: any[]): void {
            // 静态数据生成
            const staticMenuData = handleGenerateStaticMenuData(staticRoutes)
            // 动态数据生成
            const dynamicMenuData = handleGenerateMenuData(metaRouterList)
            // 合并菜单数据
            this.$state.menuRouters = [...staticMenuData, ...dynamicMenuData]
        },
        // 重新加载菜单
        reloadMenu() {
            handleReloadMenu(this.$state.menuRouters)
        }
    },
})


/**
 * 过滤导航菜单
 * 筛选出meta中visible为true的路由节点（当某一节点的visible为false时，其子节点全部不要）
 */
const siderMenuFilter = (staticRoutes: any[]) => {
    if (staticRoutes && staticRoutes.length > 0) {
        for (let i = 0; i < staticRoutes.length; i++) {
            const route = staticRoutes[i]
            // 是否展示
            const visible = route?.meta?.visible
            // 是否拥有角色
            const hasRole = hasRouteRole(route?.meta?.role as string[])
            if (!visible || !hasRole) {
                staticRoutes.splice(i, 1)
                i--
            } else {
                if (isEqual(route.component,Layout)) {
                    route.type = 'layout'
                } else if (isEqual(route.component, MiddleView)) {
                    route.type = 'directory'
                } else if (isEqual(route.component,IFrame)) {
                    route.type = 'link'
                } else {
                    route.type = 'page'
                }
                if (route.children && route.children.length > 0) {
                    siderMenuFilter(route.children)
                }
            }
        }
    }
}

/**
 * 处理各个层级 Component
 * @param metaRouterList
 */
const handleRouterComponent = (metaRouterList: Array<RouterType>) => {
    if (metaRouterList && metaRouterList.length > 0) {
        metaRouterList.forEach(route => {
            if (route.children && route.children.length > 0) {
                // 页面下有子页面的情况
                if (route.type === 'page') {
                    handleSetComponent(route)
                } else if (route.type === 'link') {
                    route.component = IFrame
                } else {
                    // 顶级节点
                    if (route.parentId === '0') {
                        route.component = Layout
                    }
                    // 非顶级节点
                    else {
                        route.component = MiddleView
                    }
                }
                handleRouterComponent(route.children);
            }
            // 页面组件设置 components
            else {
                if (route.type === 'link') {
                    route.component = IFrame
                } else {
                    handleSetComponent(route)
                }
            }
        })
    }
}
// 设置动态路由的component
const handleSetComponent = (route: RouterType) => {
    for (const path in modules) {
        const dir = path.split('views')[1]
        if (dir === route.component) {
            route.component = () => modules[path]()
        }
    }
    if (typeof route.component === 'string') {
        route.danger = true
        route.meta.title = "项目路径下没有找到 src/views" + route.component + " 资源"
    }
}

// 生成静态菜单数据
const handleGenerateStaticMenuData = (staticRoutes: any[]): ItemType[] => {
    // 过滤导航栏分配type
    siderMenuFilter(staticRoutes)
    return handleGenerateMenuData(staticRoutes)
}

// 生成动态菜单数据
const handleGenerateMenuData = (sidebarRouters:RouterType[]): ItemType[] => {
    const childrenItemType: ItemType[] = []
    sidebarRouters.forEach(sidebar => {
        // 是否显示菜单
        if (sidebar.meta.visible) {
            // 菜单类型生成的对象包含children
            if (sidebar.type === 'directory') {
                if (sidebar.children && sidebar.children.length > 0) {
                    const resp = handleGenerateMenuData(sidebar.children)
                    // 存在子集
                    if (resp && resp.length > 0) {
                        const menuItem: ItemType = {
                            key: sidebar.key,
                            icon: () => sidebar.meta.icon ? h(Icon, {icon: sidebar.meta.icon}) : h('template'),
                            label: sidebar.meta.label,
                            children: resp,
                            type: isSiderGroup()
                        }
                        childrenItemType.push(menuItem)
                    }
                } else {
                    // 不存在子集
                    const menuItem: ItemType = {
                        key: sidebar.key,
                        icon: () => sidebar.meta.icon ? h(Icon, {icon: sidebar.meta.icon}) : h('template'),
                        label: sidebar.meta.label,
                        children: [],
                        type: isSiderGroup()
                    }
                    childrenItemType.push(menuItem)
                }
            } else if (sidebar.type === 'layout') {
                if (sidebar.children && sidebar.children.length > 0) {
                    const resp = handleGenerateMenuData(sidebar.children)
                    resp.forEach(item => {
                        childrenItemType.push(item)
                    })
                }
            }
            // 其余类型（页面/静态路由）
            else {
                const menuItem: ItemType = {
                    key: sidebar.key ? sidebar.key : sidebar.path,
                    icon: () => sidebar.meta.icon ? h(Icon, {icon: sidebar.meta.icon}) : h('template'),
                    label: sidebar.meta.label,
                    danger: sidebar.danger,
                    title: sidebar.meta.title
                }
                childrenItemType.push(menuItem)
            }
        }
    })

    return childrenItemType;
}

// 重新加载菜单导航
const handleReloadMenu = (menuRouters: any[]) => {
    menuRouters.forEach(router => {
        if (router.children) {
            handleReloadMenu(router.children)
            router.type = isSiderGroup()
        }
    })
}

// 是否为分组菜单
const isSiderGroup = (): 'group' | undefined => {
    if (!themeStore) {
        themeStore = useThemeStore();
    }
    return themeStore.siderGroup ? 'group' : undefined
}