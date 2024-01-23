import router from "./router/index";
import { useUserStore } from "@/stores/modules/user"
import { usePermissionStore } from "@/stores/modules/permission";

import token from "@/utils/token"
import NProgress from "nprogress"
import 'nprogress/nprogress.css'
const { getToken } = token
NProgress.configure({
    showSpinner: false
})

// 路由前置守卫
router.beforeEach((to,from,next) => {

    console.log('123',to.matched)
    NProgress.start()
    if (getToken()) {
        const userStore = useUserStore()
        const usePermission = usePermissionStore()
        // 判断是否拉取了用户信息
        if (Object.keys(userStore.userInfo).length === 0) {
            userStore.getUserInfo().then((resp:any) => {
                // 拉取用户信息后初始化导航信息
                usePermission.initRouters(resp.sysMenuVOList)
            })
        }
        next()
    } else {
        if (to.fullPath !== "/login") {
            next("/login")
        } else {
            next();
        }
    }

})

// 路由后置守卫
router.afterEach((to,from) => { NProgress.done() })

