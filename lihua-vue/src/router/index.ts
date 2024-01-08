import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'

// 定义路由数组
const routers = [
  {
    path: '',
    component: Layout,
    children: [
      {
        path: '/index',
        component: ()=> import("@/views/index.vue"),
        name: 'index'
      }
    ]
  }

]

// 创建路由
const router = createRouter({
  history: createWebHistory(),
  routes: routers
})
// 导出路由
export default router
