import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'

/**
 * meta..................................................配置详情
 * visible...............................................在菜单显示:true(默认) false
 * label.................................................菜单栏标题
 * title.................................................鼠标悬浮标题
 * icon..................................................菜单图标，详见 https://antdv.com/components/icon-cn 自定义图标：@/components/icon
 * viewTab...............................................在viewTab显示:true(默认) false
 * affix.................................................在viewTab固定:true false(默认)
 * viewTabSort...........................................viewTab固定下排序
 * cache.................................................缓存页面，需配置router的name属性:true false(默认)
 */

const routers = [
  {
    path: '',
    component: Layout,
    meta: { visible: true },
    children: [
        //  首页
      {
        path: '/index',
        component: () => import("@/views/index.vue"),
        meta: {
          label: '首页',
          icon: 'FastBackwardOutlined',
          viewTabSort: 1,
          affix: true,
          viewTab: true,
          visible: true
        }
      },
        // 个人中心
      {
        path: '/user',
        component: () => import("@/views/system/profile/index.vue"),
        meta: {
          label: '个人中心',
          icon: 'UserOutlined',
          noCache: false,
          affix: false,
          viewTab: true,
          visible: false
        },
      }
    ],
  },
  //   login
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/login.vue")
  },
  //   404
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

