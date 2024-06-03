import router from "./router/index";
import NProgress from "nprogress"
import 'nprogress/nprogress.css'
import { useUserStore } from "@/stores/modules/user"
import { useThemeStore } from "@/stores/modules/theme";
import token from "@/utils/token"
import { reloadLoginUser } from "@/utils/auth.ts";
const { getToken } = token
NProgress.configure({
    showSpinner: true
})

// 路由前置守卫
router.beforeEach((to,from,next) => {
    const userStore = useUserStore()
    const themeStore = useThemeStore()
    NProgress.start()
    if (getToken()) {
        // 判断是否拉取了用户信息
        if (userStore.userInfo.id === null) {
            // 拉取登录用户数据，并初始化store
            reloadLoginUser().then(() => {
                next({...to,replace: true})
            }).catch((err) => {
                console.error(err)
                userStore.clearUserInfo(err)
            })
        } else {
            next()
        }
    } else {
        themeStore.resetState()
        if (to.path !== "/login") {
            next("/login")
        } else {
            next();
        }
    }
})

// 路由后置守卫
router.afterEach((to,from) => { NProgress.done() })
