import router from "@/router";
import {useUserStore} from "@/stores/user.ts";
import {usePermissionStore} from "@/stores/permission.ts";
import {useViewTabsStore} from "@/stores/viewTabs.ts";
import {useThemeStore} from "@/stores/theme.ts";
import {useDictStore} from "@/stores/dict.ts";
import {cloneDeep} from 'lodash-es'

// 认证通过后加载系统所需的各种数据
export const init = () => {
  const userStore = useUserStore()
  const permissionStore = usePermissionStore()
  const viewTabsStore = useViewTabsStore()
  const themeStore = useThemeStore()
  const dictStore = useDictStore()

  return new Promise((resolve, reject) => {
    userStore.initUserInfo().then((resp) => {
      try {
        const metaRouterList = resp.data?.routers || []
        const staticRoutes = router.options.routes
        // 初始化系统主题（首先初始化主题，因为后续的初始化需要用到主题数据）
        themeStore.init(userStore.$state.userInfo.theme)

        // 初始化动态路由
        permissionStore.initDynamicRouter(metaRouterList)

        // 初始化用户菜单数据
        permissionStore.initMenu(metaRouterList, cloneDeep(staticRoutes) as any[])

        // 初始化totalViewTabs数据
        viewTabsStore.initTotalViewTabs(resp.data.viewTabs, cloneDeep(staticRoutes) as any[])

        // 设置最近使用组件的缓存key值
        viewTabsStore.setViewCacheKey(userStore.$state.username)

        // 清空字典store
        dictStore.clearDict()

        // 清空组件keep-alive
        viewTabsStore.clearComponentsKeepAlive()

        resolve(resp)
      } catch (e) {
        // 此处产生了异常应该由then中各个方法处理，这里只进行打印即可
        console.error(e)
        reject(e)
      }
    }).catch(e => {
      reject(e)
    })
  })
}
