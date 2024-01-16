import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'

const routers = [
  {
    path: '',
    component: Layout,
    name: 'home',
    children: [
      {
        path: 'index',
        component: () => import("@/views/index.vue"),
        name: 'Index'
      }
    ]
  },
  {
    path: '/test',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import("@/views/Test.vue"),
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: routers
})

export default router
