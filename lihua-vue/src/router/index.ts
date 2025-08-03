import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'
import MiddleView from '@/components/middle-view/index.vue'
import Iframe from '@/components/iframe/index.vue'
/**
 * 说明：静态路由基于vue router，在项目中进行了逻辑定制
 * 1. meta 中 visible 设置为 true 的菜单/页面将会在导航中显示
 * 2. 显示的菜单中
 *    components 为 Layout 的层级将不进行显示
 *    components 为 MiddleView 的层级将显示为菜单
 *    components 为 Iframe 的层级将显示为链接地址，同时需要指定 meta.linkOpenType ('inner' | 'new-page')
 *    components 为 () => import('@/views/**.vue') 的层级显示为一般组件
 * 3. 页面中更详细的配置见 meta 配置
 */

/**
 * components................................................配置详情
 * Layout...................................................页面在layout下，如果存在的话，为路由书中的最顶级节点
 * MiddleView...............................................对应导航菜单，指定component为MiddleView的话，导航中为菜单
 * Iframe...................................................对应导航中链接类型的页面，需要在meta中指定linkOpenType
 * () => import('@/views/**.vue') ..........................对应导航中的组件
 */

/**
 * meta.....................................................配置详情
 *    visible...............................................在菜单中显示：true(默认)、false
 *    label.................................................菜单栏标题
 *    title.................................................鼠标悬浮显示的标题
 *    icon..................................................菜单图标，直接配置组件名即可。详见 https://antdv.com/components/icon-cn 自定义图标：@/components/icon
 *    viewTab...............................................在viewTab中显示：true(默认)、false
 *    affix.................................................在viewTab中固定：true、false(默认)
 *    viewTabSort...........................................固定在viewTab下的标签排序
 *    cache.................................................缓存页面，需要配置router的name属性：true、false(默认)
 *    linkOpenType..........................................标记当前路由类型为link情况下的打开方式：'inner' | 'new-page'
 *    link..................................................link的链接地址
 *    role..................................................标记哪些角色的用户可访问此页面/目录及以下页面/目录：['ROLE_admin','ROLE_normal',...]，不配置或配置为[]则所有用户均可访问
 *    allowAnonymous........................................是否允许在未登录的情况下访问：true、false（默认）
 */

const routers = [
  // 重定向到首页
  {
    path: '',
    alias:['/','/root','/home'],
    redirect: '/index'
  },
  {
    path: '',
    component: Layout,
    meta: { visible: true },
    children: [
      // 首页
      {
        path: '/index',
        component: () => import("@/views/index/index.vue"),
        name: 'AppIndex',
        meta: {
          label: '首页',
          icon: 'HomeOutlined',
          viewTabSort: 1,
          affix: true,
          viewTab: true,
          visible: true
        }
      },
        // 个人中心
      {
        path: '/profile',
        component: () => import("@/views/system/profile/SystemProfile.vue"),
        name: 'SystemProfile',
        meta: {
          label: '个人中心',
          icon: 'UserOutlined',
          cache: false,
          affix: false,
          viewTab: true,
          visible: false
        },
      },
      {
        path: '/setting',
        component: () => import("@/views/system/setting/SystemSetting.vue"),
        name: 'SystemSetting',
        meta: {
          label: "系统设置",
          icon: "SettingOutlined",
          cache: false,
          affix: false,
          viewTab: true,
          visible: false,
          role: ["ROLE_admin", "ROLE_visitor"]
        }
      }
    ],
  },
  // login
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/login/index.vue")
  },
  // 407
  {
    path: "/407",
    component: () => import("@/views/error/407/index.vue"),
    meta: {
      allowAnonymous: true
    }
  },
  // 403
  {
    path: "/403",
    component: () => import("@/views/error/403/index.vue"),
  },
  // 404
  {
    path: "/:pathMatch(.*)*",
    component: () => import("@/views/error/404/index.vue"),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes: routers
})

export default router

