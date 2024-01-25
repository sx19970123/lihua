import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'
import type {Component} from "vue";
import type {Route} from "ant-design-vue/es/breadcrumb/Breadcrumb";

/**
 * 定义路由配置对象
 */
interface RouteConfig {
    path: string,
    component?: Component,
    name?: string,
    components?: { [name: string]: Component },
    redirect?: string | Location | Function,
    alias?: string | Array<string>,
    children?: Array<RouteConfig>,
    meta?: any,

    beforeEnter?: (to: Route, from: Route, next: Function) => void,
    props?: boolean | Object | Function,
    caseSensitive?: boolean,
    pathToRegexpOptions?: Object
}

/**
 * 定义路由元数据对象
 * noCache：如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
 * label：侧边导航栏 / 顶部导航条 页面名称
 * title：鼠标悬浮显示内容
 * icon：对应图标
 * affix：常驻固定顶部导航条
 * menuShow：
 */
interface MetaConfig {
    noCache?: boolean,
    label?: string,
    title?: string,
    icon?: string | Component,
    affix?: boolean,
    viewTabSort: number
}

const routers = [
  {
    path: '',
    component: Layout,
    children: [
      {
        path: '/index',
        component: () => import("@/views/index.vue"),
        meta: { label: '首页', icon: 'FastBackwardOutlined', affix: true, viewTabSort: 1 }
      }
    ],
  },
  {
    path: '/login',
    name: 'Login',
    hidden: true,
    component: () => import("@/views/login.vue")
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: routers
})

export default router

