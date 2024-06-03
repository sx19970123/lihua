import { defineStore } from "pinia";
import type {RouterType} from "@/api/type.ts";
import type {RouteRecordRaw} from "vue-router";
import Layout from "@/layout/index.vue";
import router from "@/router";
import IFrame from "@/components/iframe/index.vue";
import MiddleView from "@/components/middle-view/index.vue";

// 获取 views 下的所有 vue 组件
const modules = import.meta.glob("../../views/**/*.vue")

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
        // 初始化动态路由
        initDynamicRouter(metaRouterList: Array<RouterType>) {
            // 处理各个层级 Component
            handleRouterComponent (metaRouterList)
            // 顶级无父组件目录、页面添加layout父级
            metaRouterList.forEach(route => {
                const isRoot =  route.parentId === '0' &&  ( route.children === null || route.children.length === 0)
                const isPageType = route.type === 'page';
                const isMenuType = route.type === 'directory';
                const isLinkType = route.type === 'link' && route.meta.linkOpenType === 'inner';
                if ((isPageType || isLinkType || isMenuType) && isRoot) {
                    const parentRoute: RouteRecordRaw  = {
                        children: [route as any],
                        path: "",
                        component: Layout,
                        name: route.name + "Parent"
                    };
                    router.addRoute(parentRoute);
                } else {
                    router.addRoute(route as any);
                }
            });
        },
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
 * @param key
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
    for (let path in modules) {
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
