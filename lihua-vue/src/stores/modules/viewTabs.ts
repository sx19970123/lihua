import { defineStore } from "pinia";
import type {RouteRecordRaw} from "vue-router";
import Layout from "@/layout/index.vue";
import { format } from "@/utils/date";
import type { StarViewType,RecentType } from "@/api/system/star-view/type/starView"
import type {RouterType} from "@/api/system/user/type/router";
import Login from "@/views/login.vue";
export const useViewTabsStore = defineStore('viewTabs',{
    state: () => {
        const viewTabs: Array<StarViewType> = []
        const totalViewTabs: Array<StarViewType> = []
        const componentAlive: Array<string> = []
        const activeKey: string = ''
        const tabCacheKey: string = ''
        return {
            viewTabs,
            totalViewTabs,
            activeKey,
            tabCacheKey,
            componentAlive
        }
    },
    actions: {
        /**
         * 数据初始化
         * @param starViewVOList
         * @param staticRoutes
         */
        initTotalViewTabs(starViewVOList: Array<StarViewType>, staticRoutes: any[]): void {
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
                        static: true,
                        query: route.meta.query
                    })
                })
            }

            // 全量数据
            this.$state.totalViewTabs = starViewVOList
            // 默认显示数据
            this.$state.viewTabs = this.$state.totalViewTabs.filter(tab => tab.affix)
        },

        // key值是否包含在ViewTabs之中
        isIncludeViewTabs(key: string) {
            return this.getIndex(key) !== -1
        },
        // 获取元素在数组中的索引值
        getIndex(key: string) {
            return this.$state.viewTabs.findIndex(tab => tab.routerPathKey === key)
        },
        // 根据key在total中获取tab对象
        getTotalTabByKey(key: string) {
            const index = this.$state.totalViewTabs.findIndex(tab => tab.routerPathKey === key)
            return this.$state.totalViewTabs[index]
        },
        // 根据索引获取元素
        getTabByIndex(index: number) {
            return this.$state.viewTabs[index]
        },
        // 选中tab页，skip跳过不进行view-tab 管理
        selectedViewTab(key: string,skip: boolean) {
            if (!skip) {
                const tab = this.getTotalTabByKey(key)
                if (tab) {
                    // 包含
                    if (!this.isIncludeViewTabs(key)) {
                        this.addViewTab(tab)
                    }
                    this.$state.activeKey = key
                    // 缓存数据
                    handleAddTabCache(tab)
                }
            }
        },
        // 新开tab页
        addViewTab(tab: StarViewType) {
            this.$state.viewTabs.push(tab)
        },
        // 关闭tab页
        closeViewTab(key: string) {
            this.$state.viewTabs = this.$state.viewTabs.filter(viewTab => viewTab.routerPathKey !== key)
        },
        // 关闭左边
        closeLeft(key: string): Array<string> {
            const index:number = this.getIndex(key)
            const viewTabs:StarViewType[] = this.$state.viewTabs
            const removeArray:string[] = []
            for (let i = 0; i < index; i++) {
                if (!viewTabs[i].affix) {
                    removeArray.push(viewTabs[i].routerPathKey)
                }
            }
            this.$state.viewTabs = viewTabs.filter((tab:StarViewType) => !removeArray.includes(tab.routerPathKey))
            return removeArray
        },
        // 关闭右边
        closeRight(key: string): Array<string> {
            const index:number = this.getIndex(key)
            const viewTabs:StarViewType[] = this.$state.viewTabs
            const removeArray:string[] = []
            for (let i = index + 1; i < viewTabs.length; i++) {
                if (!viewTabs[i].affix) {
                    removeArray.push(viewTabs[i].routerPathKey)
                }
            }
            this.$state.viewTabs = viewTabs.filter((tab:StarViewType) => !removeArray.includes(tab.routerPathKey))
            return removeArray
        },
        // 关闭其他
        closeOther(key: string): Array<string> {
            const index:number = this.getIndex(key)
            const viewTabs:StarViewType[] = this.$state.viewTabs
            const removeArray:string[] = []
            for (let i = 0; i < viewTabs.length; i++) {
                if (!viewTabs[i].affix && i !== index) {
                    removeArray.push(viewTabs[i].routerPathKey)
                }
            }
            this.$state.viewTabs = viewTabs.filter((tab:StarViewType) => !removeArray.includes(tab.routerPathKey))
            return removeArray
        },
        // 关闭全部
        closeAll(): Array<string> {
            const removeArray = this.$state.viewTabs.filter(tab => !tab.affix).map(tab => tab.routerPathKey)
            this.$state.viewTabs = this.$state.viewTabs.filter(tab => tab.affix)
            return removeArray
        },
        // 传入tab元素，与集合中的元素进行替换
        replaceByKey(tab: StarViewType) {
            // 替换viewTabs
            const index = this.$state.viewTabs.findIndex(t => t.routerPathKey === tab.routerPathKey)
            this.$state.viewTabs.splice(index,1, tab)
            // 替换totalViewTabs
            const totalIndex = this.$state.totalViewTabs.findIndex(t => t.routerPathKey === tab.routerPathKey)
            this.$state.totalViewTabs.splice(totalIndex,1, tab)
        },
        // 添加固定，固定到前排
        affix(tab: StarViewType) {
            const targetIndex = this.$state.viewTabs.filter(t => t.affix).length
            const viewTabs = this.$state.viewTabs
            const index = viewTabs.findIndex(t => t.routerPathKey === tab.routerPathKey)
            viewTabs.splice(index,1)
            this.$state.viewTabs.splice(targetIndex,0,tab)
        },
        // 取消固定，移动到最后
        unAffix(tab: StarViewType) {
            const viewTabs = this.$state.viewTabs
            const index = viewTabs.findIndex(t => t.routerPathKey === tab.routerPathKey)
            viewTabs.splice(index,1)
            viewTabs.splice(viewTabs.length,0,tab)
        },
        // 设置缓存viewCache key
        setViewCacheKey(username:string): void {
            this.$state.tabCacheKey = 'recent-tabs-' + username
        },
        // 设置组件缓存
        setComponentsKeepAlive(componentName: string) {
            if (!this.$state.componentAlive.includes(componentName)) {
                this.$state.componentAlive.push(componentName)
            }
        },
        // 删除组件缓存（index.vue的组件默认全部缓存）
        removeComponentsKeepAlive(componentName: string) {
            if (componentName !== 'index') {
                this.$state.componentAlive = this.$state.componentAlive.filter(item => item !== componentName)
            }
        }
    }
})

/**
 * 处理 router/index 中静态路由，生成 key （路由path拼接）
 * @param routers
 */
const generateKey = (routers: any[], key: string, filter: boolean): Array<RouterType> => {
    let menuShowList: any[]
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
 * 获取最底层子节点
 * @param staticRoutes
 * @param arr
 */
const getChildren = (staticRoutes: any[], arr:  Array<StarViewType>): void => {
    if (staticRoutes) {
        staticRoutes.forEach(route => {
            if (route.children && route.children.length > 0) {
                if (route.component !== Layout) {
                    arr.push(route)
                }
                getChildren(route.children,arr)
            } else {
                arr.push(route)
            }
        })
    }
}

/**
 * 向 localStorage 中缓存
 * @param tab
 */
const handleAddTabCache = (tab: StarViewType) => {
    const viewTabStore =  useViewTabsStore()
    const recentTabs = localStorage.getItem(viewTabStore.$state.tabCacheKey)
    // 第一次新建缓存集合
    if (recentTabs === null) {
        localStorage.setItem(viewTabStore.$state.tabCacheKey ,JSON.stringify([
            {
                openTime: format(new Date(),'yyyy-MM-dd hh:mm'),
                icon: tab.icon,
                label: tab.label,
                path: tab.routerPathKey
            }
        ]))
    } else {
        const hisArray: Array<RecentType> = JSON.parse(recentTabs)
        const index = hisArray.findIndex((his: RecentType) => his.path === tab.routerPathKey)
        // 删除已存在元素
        if (index !== -1) {
            hisArray.splice(index,1)
        }
        // 首插后存入缓存
        hisArray.unshift({
            openTime: format(new Date(),'yyyy-MM-dd hh:mm'),
            icon: tab.icon,
            label: tab.label,
            path: tab.routerPathKey
        })
        localStorage.setItem(viewTabStore.$state.tabCacheKey ,JSON.stringify(hisArray))
    }
}
