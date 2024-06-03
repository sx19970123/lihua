import type {ResponseType} from "@/api/type.ts";
import type {UserInfoType} from "@/api/system/profile/type/user.ts";

import router from "@/router";
import {useUserStore} from "@/stores/modules/user.ts";
import {usePermissionStore} from "@/stores/modules/permission.ts";
import {useViewTabsStore} from "@/stores/modules/viewTabs.ts";
import {useThemeStore} from "@/stores/modules/theme.ts";
import {useDictStore} from "@/stores/modules/dict.ts";

// 认证通过后加载系统所需的各种数据
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
      permissionStore.initDynamicRouter(metaRouterList)
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