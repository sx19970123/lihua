import router from "./router/index";
import NProgress from "nprogress"
import 'nprogress/nprogress.css'
import {useUserStore} from "@/stores/user"
import {useThemeStore} from "@/stores/theme";
import token from "@/helpers/token.ts"
import {initApp} from "@/app-init.ts";
import {hasRouteRole} from "@/helpers/auth.ts";
import {closeConnect, connect} from "@/utils/web-socket.ts";

const { getToken } = token

// 路由前置守卫
router.beforeEach(async (to, from, next) => {
    NProgress.start()
    const userStore = useUserStore()
    const themeStore = useThemeStore()
    const hasToken = getToken()
    if (hasToken) {
        try {
            // 判断是否已拉取用户信息
            if (!userStore.userInfo.id) {
                // 拉取登录用户数据，并初始化 store
                await initApp();
                // 检查登录设置
                if (to.name === 'Login') {
                    const data = await userStore.checkUserAfterLogin()
                    if (data.length > 0) {
                        next(false)
                        return
                    }
                }
                // 连接到websocket
                await connect()
                // 判断用户是否拥有静态路由中指定的角色
                if (hasRouteRole(to?.meta?.role as string[])) {
                    // 已登录状态下，请求登录页面自动跳转到首页
                    to.path === "/login" ? next('/index') : next({ ...to, replace: true })
                } else {
                    next("/403")
                }
            } else {
                if (hasRouteRole(to?.meta?.role as string[])) {
                    next();
                } else {
                    next("/403")
                }
            }
        } catch (error) {
            console.error(error)
            // 关闭websocket连接
            closeConnect()
            // 清空用户信息
            userStore.clearUserInfo()
            // 重定向到登录页面
            next({name: "Login"})
        }
    } else {
        // 重置主题
        themeStore.resetState();
        // 关闭websocket连接
        closeConnect()
        if (to.meta && to.meta.allowAnonymous) {
            next()
        } else {
            to.path !== "/login" ? next({name: "Login"}) : next()
        }
    }

    NProgress.done();
});

// 路由后置守卫
router.afterEach((to,from) => { NProgress.done() })
