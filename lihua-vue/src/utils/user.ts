import type {ResponseType} from "@/api/type.ts";
import type {UserInfoType} from "@/api/system/profile/type/user.ts";
import Layout from "@/layout/index.vue";
import MiddleView from "@/components/middle-view/index.vue"
import IFrame from "@/components/iframe/index.vue";

import router from "@/router";
import {useUserStore} from "@/stores/modules/user.ts";
import {usePermissionStore} from "@/stores/modules/permission.ts";
import {useViewTabsStore} from "@/stores/modules/viewTabs.ts";
import {useThemeStore} from "@/stores/modules/theme.ts";
import {useDictStore} from "@/stores/modules/dict.ts";
import type {RouterType} from "@/api/system/profile/type/router.ts";
import type {RouteRecordRaw} from "vue-router";

// 获取 views 下的所有 vue 组件
const modules = import.meta.glob("../views/**/*.vue")
// 重新加载用户数据和store
export const reloadLoginUser = () => {
  const userStore = useUserStore()
  const permissionStore = usePermissionStore()
  const viewTabsStore = useViewTabsStore()
  const themeStore = useThemeStore()
  const dictStore = useDictStore()
  return new Promise((resolve, reject) => {
    userStore.getUserInfo().then((resp: ResponseType<UserInfoType>) => {
      const metaRouterList = resp.data?.routerList || []
      const staticRoutes = router.options.routes
      // 初始化动态路由
      initDynamicRouter(metaRouterList)
      // 初始化用户菜单数据
      permissionStore.initMenu(metaRouterList, staticRoutes as any[])
      // 初始化totalViewTabs数据
      viewTabsStore.initTotalViewTabs(resp.data?.viewTabList || [], staticRoutes as any[])
      // 设置最近使用组件的缓存key值
      viewTabsStore.setViewCacheKey(resp.data?.username || '')
      // 初始化系统主题
      themeStore.init(resp.data.user.theme)
      // 清空字典store
      dictStore.clearDict()
      // 清空组件keep-alive
      viewTabsStore.clearComponentsKeepAlive()

      resolve('load success')
    }).catch(err => {
      reject(err)
    })
  })
}

/**
 * 初始化动态路由
 * @param metaRouterList
 */
const initDynamicRouter = (metaRouterList: Array<RouterType>): void => {
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
