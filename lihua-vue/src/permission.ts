import router from "./router/index";
import { useUserStore } from "@/stores/modules/user"
import { usePermissionStore } from "@/stores/modules/permission";

import token from "@/utils/token"
import NProgress from "nprogress"
import 'nprogress/nprogress.css'
import type {Component} from "vue";
const { getToken } = token
NProgress.configure({
    showSpinner: false
})

// 路由前置守卫
router.beforeEach((to,from,next) => {
    NProgress.start()
    if (getToken()) {
        const userStore = useUserStore()
        const usePermission = usePermissionStore()
        // 判断是否拉取了用户信息
        if (Object.keys(userStore.userInfo).length === 0) {
            userStore.getUserInfo().then(resp => {
                // 拉取用户信息之后初始化导航菜单路由信息
                usePermission.initRouters(resp.sysMenuTreeList)
                // 初始化动态路由
                initDynamicRoutes(resp.sysMenuTreeList)
            })
        }
        next()
    } else {
        if (to.fullPath !== "/login") {
            next("/login")
        } else {
            next();
        }
        NProgress.done()
    }

})

// 路由后置守卫
router.afterEach((to,from) => { NProgress.done() })

// 初始化路由
const initRoute = () => {
    interface MetaType {
        // 导航名称
        label: string,
        // 鼠标悬浮标题
        title: string,
        // 设置为true则不进行 keep-alive 缓存
        noCache: boolean,
        // 链接类型地址
        link: string
    }
    interface RouteType {
        // 组件名称，路径首字母大写
        name: string,
        // 组件路径
        path: string,
        // 组件路径，一级菜单为Layout，中间菜单为 MiddleView，页面为对应路由地址。由 import 进行导入
        component: Component,
        // query参数
        query: string,
        // 路由元信息，守卫中可获取进行处理，自定义数据
        meta: MetaType,
        // 子数据
        children?: Array<RouteType>
    }


// 初始化动态路由
    const initDynamicRoutes = (routes) => {
        routes.forEach(metaRoure => {
            if (metaRoure.children !== null) {
                let route :RouteType = {
                    name: '',
                    path: '',
                    component: '',
                    query: '',
                    meta: '',
                    children: []
                }

                recursionRoutes(metaRoure.children);
            }



            console.log("metaRoure",metaRoure)
        })
    }

    const recursionRoutes = (routes) => {
        routes.forEach(metaRoure => {
            if (metaRoure.children !== null) {
                recursionRoutes(metaRoure.children);
            }
        })
    }


// 添加动态路由
    const addDynamicRoutes = () => {
        router.addRoute({

        })
        console.log("初始化动态路由:" ,resp)
    }


    return {
        initDynamicRoutes,
        addDynamicRoutes
    }
}

const {initDynamicRoutes , addDynamicRoutes} = initRoute()
