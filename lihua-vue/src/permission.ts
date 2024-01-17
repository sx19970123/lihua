import router from "./router/index";
import token from "@/utils/token"
import NProgress from "nprogress"
import 'nprogress/nprogress.css'
const { getToken } = token
NProgress.configure({
    showSpinner: false
})



// 路由前置守卫
router.beforeEach((to,from,next) => {
    NProgress.start()
    if (getToken()) {
        console.log(to)
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
