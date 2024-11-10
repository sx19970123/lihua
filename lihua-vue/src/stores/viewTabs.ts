import { defineStore } from "pinia";
import Layout from "@/layout/index.vue";
import MiddleView from "@/components/middle-view/index.vue";
import Iframe from "@/components/iframe/index.vue";
import type { StarViewType,RecentType } from "@/api/system/view-tab/type/SysViewTab.ts"
import dayjs from "dayjs";
import type {RouteLocationNormalizedLoaded} from "vue-router";
import {hasRouteRole} from "@/utils/Auth.ts"
import {isEqual} from "lodash-es"
import {v4 as uuidv4} from "uuid";
import {cloneDeep} from "lodash-es";
export const useViewTabsStore = defineStore('viewTabs',{
    state: () => {
        // viewTab 标签页数组
        const viewTabs: Array<StarViewType> = []
        // 全部的viewTab
        const totalViewTabs: Array<StarViewType> = []
        // 缓存组件名集合
        const componentAlive: Array<string> = []
        // 当前标签页key
        const activeKey: string = ''
        // 最近使用缓存key
        const tabCacheKey: string = ''
        // layout中content组件key值，修改以重新加载组件
        const contentComponentKey: string = ''
        // 显示layout
        const showLayout: boolean = 'hide' !== localStorage.getItem("show-hide-layout")
        return {
            viewTabs,
            totalViewTabs,
            activeKey,
            tabCacheKey,
            componentAlive,
            contentComponentKey,
            showLayout
        }
    },
    actions: {
        /**
         * 数据初始化
         * @param viewTabVOList
         * @param staticRoutes
         */
        initTotalViewTabs(viewTabVOList: Array<StarViewType>, staticRoutes: any[]): void {
            // 去除父级节点获取子路由组件
            const hasKeyRoutComponentList: Array<StarViewType> = []
            getStaticItem(staticRoutes,hasKeyRoutComponentList)
            // 根据定义的 viewTabSort 进行排序
            hasKeyRoutComponentList.sort((a, b) => {
                const num1 = a.viewTabSort ? a.viewTabSort : 99999
                const num2 = b.viewTabSort ? b.viewTabSort : 99999
                return  num2 - num1
            })
            // 生成viewTab对象
            hasKeyRoutComponentList.forEach(route => {
                viewTabVOList.unshift(route)
            })

            // 全量数据
            this.$state.totalViewTabs = viewTabVOList
            // 默认显示数据
            this.$state.viewTabs = this.$state.totalViewTabs.filter(tab => tab.affix)
            // 更新显示隐藏layout
            this.setShowLayoutAttribute()
        },
        // 根据路由信息加载viewTag
        init(route: RouteLocationNormalizedLoaded) {
            // 通过viewTab进行标签管理
            if (route?.meta?.viewTab) {
                this.selectedViewTab(route.path,route?.meta?.viewTab as boolean)
            }
            // 跳过标签管理
            else {
                // 当前组件为跳过，默认选中其父组件
                const unSkipList =  route.matched.filter(item => item?.meta?.viewTab && item.path !== '/')
                if (unSkipList && unSkipList.length > 0) {
                    // 选中接收view-tabs托管的父组件
                    this.selectedViewTab(unSkipList[unSkipList.length - 1].path, route?.meta?.viewTab as boolean)
                }
            }
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
            return cloneDeep(this.$state.totalViewTabs[index])
        },
        // 根据key在当前ViewTabs获取tab对象
        getViewTabsByKey(key: string) {
            const index = this.$state.viewTabs.findIndex(tab => tab.routerPathKey === key)
            return this.getTabByIndex(index)
        },
        // 根据索引获取元素
        getTabByIndex(index: number) {
            return this.$state.viewTabs[index]
        },
        // 选中tab页，skip跳过不进行view-tab 管理
        selectedViewTab(key: string, viewTab: boolean, tempQuery?: string) {
            if (!viewTab) {
                // 取消当前选中所有的 tab
                this.$state.activeKey = '';
                return;
            }

            let tab = this.getViewTabsByKey(key);
            const isExistingTab = !!tab;

            // 如果 tab 不存在，则从 totalTabs 中查找
            if (!tab) {
                tab = this.getTotalTabByKey(key);
            }

            // 处理 query 合并
            if (tempQuery) {
                tab.query = this.mergeTabQuery(tab.query, tempQuery);
            }

            // 如果 tab 是新添加的，执行 addViewTab 操作
            if (!isExistingTab) {
                this.addViewTab(tab);
            }

            // 更新选中的 tab
            this.$state.activeKey = key;

            // 缓存数据
            handleAddTabCache(tab);
        },
        mergeTabQuery(originQuery?: string, tempQuery?: string): string {
            if (!tempQuery) return originQuery || '';

            try {
                const tempQueryObj = JSON.parse(tempQuery);

                if (originQuery) {
                    const originQueryObj = JSON.parse(originQuery);
                    Object.assign(originQueryObj, tempQueryObj);
                    return JSON.stringify(originQueryObj);
                }

                return tempQuery;
            } catch (error) {
                console.error("解析 query 失败:", error);
                return originQuery || '';
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
        setComponentsKeepAlive(name: string) {
            this.$state.componentAlive.push('index')
            if (!this.$state.componentAlive.includes(name)) {
                this.$state.componentAlive.push(name)
            }
        },
        // 删除组件缓存
        removeComponentsKeepAlive(name: string) {
            this.$state.componentAlive = this.$state.componentAlive.filter(item => item !== name)
        },
        // 清空组件缓存
        clearComponentsKeepAlive() {
            this.$state.componentAlive = []
        },
        // 重新生成 contentComponentKey
        regenerateComponentKey() {
            // 生成一个随机的 UUID
            this.$state.contentComponentKey = uuidv4();
        },
        // 修改显示layout后更新html节点show-hide-layout
        setShowLayoutAttribute () {
            document.documentElement.setAttribute("show-hide-layout", 'hide' === localStorage.getItem("show-hide-layout")  ? 'hide' : 'show')
        }
    }
})

/**
 * 获取page 或 link 节点的数据
 * @param staticRoutes
 * @param arr
 */
const getStaticItem = (staticRoutes: any[], arr: Array<StarViewType>): void => {
    if (staticRoutes) {
        staticRoutes.forEach(route => {

            if (hasRouteRole(route?.meta?.role as string[]) && !isEqual(route.component, Layout) && !isEqual(route.component, MiddleView)) {
                const item: StarViewType = {
                    affix: route.meta?.affix,
                    icon: route.meta?.icon,
                    label: route.meta?.label,
                    link: route.meta?.link,
                    linkOpenType: route.meta?.linkOpenType,
                    viewTabSort: route.meta?.viewTabSort,
                    routerPathKey: route.path,
                    static: true
                }
                if (isEqual(route.component, Iframe)) {
                    item.menuType = 'link'
                } else {
                    item.menuType = 'page'
                }
                arr.push(item);
            }

            if (route.children && route.children.length > 0) {
                getStaticItem(route.children, arr);
            }
        });
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
                openTime: dayjs().format('YYYY-MM-DD HH:mm'),
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
            openTime: dayjs().format('YYYY-MM-DD HH:mm'),
            icon: tab.icon,
            label: tab.label,
            path: tab.routerPathKey
        })
        localStorage.setItem(viewTabStore.$state.tabCacheKey ,JSON.stringify(hisArray))
    }
}
