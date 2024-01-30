import { defineStore } from "pinia";
import type {RouteRecordRaw} from "vue-router";
import Layout from "@/layout/index.vue";
import {format} from "@/utils/date";
export const useViewTabsStore = defineStore('viewTabs',{
    state: () => {
        const viewTabs: Array<any> = []
        const totalViewTabs: Array<any> = []
        const activeKey: string = ''
        const showRecentModal: boolean = false
        const showStarModal: boolean = false
        return {
            viewTabs,
            totalViewTabs,
            activeKey,
            showRecentModal,
            showStarModal
        }
    },
    actions: {
        /**
         * 数据初始化
         * @param starViewVOList
         * @param staticRoutes
         */
        initTotalViewTabs(starViewVOList: Array<any>, staticRoutes: readonly RouteRecordRaw[]): void {
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

            // 全量数据
            this.$state.totalViewTabs = starViewVOList
            // 默认显示数据
            this.$state.viewTabs = this.$state.totalViewTabs.filter(tab => tab.affix)
        },

        // key值是否包含在ViewTabs之中
        isIncludeViewTabs(key: string): boolean {
            return this.getIndex(key) !== -1
        },
        // 获取元素在数组中的索引值
        getIndex(key: string): number {
            return this.$state.viewTabs.findIndex(tab => tab.routerPathKey === key)
        },
        // 根据key在total中获取tab对象
        getTotalTabByKey(key: string): any {
            const index = this.$state.totalViewTabs.findIndex(tab => tab.routerPathKey === key)
            return this.$state.totalViewTabs[index]
        },
        // 根据索引获取元素
        getTabByIndex(index: number): any {
            return this.$state.viewTabs[index]
        },
        // 选中tab页
        selectedViewTab(key: string) {
            const tab = this.getTotalTabByKey(key)
            // 包含
            if (!this.isIncludeViewTabs(key)) {
                this.addViewTab(tab)
            }
            this.$state.activeKey = key
            // 缓存数据
            handleAddTabCache(tab)
        },
        // 新开tab页
        addViewTab(tab: any) {
            this.$state.viewTabs.push(tab)
        },
        // 关闭tab页
        closeViewTab(key: string) {
            this.$state.viewTabs = this.$state.viewTabs.filter(viewTab => viewTab.routerPathKey !== key)
        },
        // 关闭左边
        closeLeft(key: string) {
            const index:number = this.getIndex(key)
            const viewTabs:any[] = this.$state.viewTabs
            const removeArray:any[] = []
            for (let i = 0; i < index; i++) {
                if (!viewTabs[i].affix) {
                    removeArray.push(viewTabs[i])
                }
            }
            this.$state.viewTabs = viewTabs.filter((tab:any) => !removeArray.includes(tab))
        },
        // 关闭右边
        closeRight(key: string) {
            const index:number = this.getIndex(key)
            const viewTabs:any[] = this.$state.viewTabs
            const removeArray:any[] = []
            for (let i = index + 1; i < viewTabs.length; i++) {
                if (!viewTabs[i].affix) {
                    removeArray.push(viewTabs[i])
                }
            }
            this.$state.viewTabs = viewTabs.filter((tab:any) => !removeArray.includes(tab))
        },
        // 关闭其他
        closeOther(key: string) {
            const index:number = this.getIndex(key)
            const viewTabs:any[] = this.$state.viewTabs
            const removeArray:any[] = []
            for (let i = 0; i < viewTabs.length; i++) {
                if (!viewTabs[i].affix && i !== index) {
                    removeArray.push(viewTabs[i])
                }
            }
            this.$state.viewTabs = viewTabs.filter((tab:any) => !removeArray.includes(tab))
        },
        // 关闭全部
        closeAll() {
            this.$state.viewTabs = this.$state.viewTabs.filter(tab => tab.affix)
        },
        // 打开最近使用模态框
        openRecentModal() {
            this.$state.showRecentModal = true
        },
        // 关闭最近使用模态框
        closeRecentModal() {
            this.$state.showRecentModal = false
        },
        // 打开收藏模态框
        openStarModal() {
            this.$state.showStarModal = true
        },
        // 关闭收藏模态框
        closeStarModal() {
            this.$state.showStarModal = false
        },
        // 传入tab元素，与集合中的元素进行替换
        replaceByKey(tab: any) {
            // 替换viewTabs
            const index = this.$state.viewTabs.findIndex(t => t.routerPathKey === tab.routerPathKey)
            this.$state.viewTabs.splice(index,1, tab)
            // 替换totalViewTabs
            const totalIndex = this.$state.totalViewTabs.findIndex(t => t.routerPathKey === tab.routerPathKey)
            this.$state.totalViewTabs.splice(totalIndex,1, tab)
        },
        // 添加固定，固定到前排
        affix(tab: any) {
            const targetIndex = this.$state.viewTabs.filter(t => t.affix).length
            const viewTabs = this.$state.viewTabs
            const index = viewTabs.findIndex(t => t.routerPathKey === tab.routerPathKey)
            viewTabs.splice(index,1)
            this.$state.viewTabs.splice(targetIndex,0,tab)
        },
        // 取消固定，移动到最后
        unAffix(tab: any) {
            const viewTabs = this.$state.viewTabs
            const index = viewTabs.findIndex(t => t.routerPathKey === tab.routerPathKey)
            viewTabs.splice(index,1)
            viewTabs.splice(viewTabs.length,0,tab)
        }
    }
})

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

/**
 * 向 localStorage 中缓存
 * @param tab
 */
const handleAddTabCache = (tab: any) => {
    const recentTabs = localStorage.getItem('recent-tabs')
    // 第一次新建缓存集合
    if (recentTabs === null) {
        localStorage.setItem('recent-tabs',JSON.stringify([
            {
                openTime: format(new Date(),'yyyy-MM-dd hh:mm'),
                icon: tab.icon,
                label: tab.label,
                path: tab.routerPathKey
            }
        ]))
    } else {
        const hisArray = JSON.parse(recentTabs)
        const index = hisArray.findIndex((his: any) => his.path === tab.routerPathKey)
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
        localStorage.setItem('recent-tabs',JSON.stringify(hisArray))
    }
}
