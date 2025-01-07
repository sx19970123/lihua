import router from "./router/index";
import NProgress from "nprogress"
import 'nprogress/nprogress.css'
import { useUserStore } from "@/stores/user"
import { useThemeStore } from "@/stores/theme";
import token from "@/utils/Token.ts"
import { init } from "@/utils/AppInit.ts";
import {hasRouteRole} from "@/utils/Auth.ts";
import {close, connect} from "@/utils/ServerSentEvents.ts";
import {message} from "ant-design-vue";
import Token from "@/utils/Token.ts";
import {getLoginSetting} from "@/api/system/login/Login.ts";
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
                await init();
                // 连接到sse
                await connect()
                // 检查登录设置
                if (to.fullPath === '/login' || !Token.getLoginSettingResult()) {
                    const loginSettingResp = await getLoginSetting()
                    if (loginSettingResp.code === 200) {
                        const data = loginSettingResp.data
                        if (data.length > 0) {
                            next({ name: 'Login', state: {settingComponentNameList: data} });
                        }
                    } else {
                        message.error(loginSettingResp.msg)
                        next("/login");
                    }
                }
                // 判断用户是否拥有静态路由中指定的角色
                if (hasRouteRole(to?.meta?.role as string[])) {
                    // 已登录状态下，请求登陆页面自动跳转到首页
                    to.path === "/login" ? next('/index') : next({ ...to, replace: true })
                } else {
                    next("/403")
                }
            } else {
                // 连接到sse
                await connect()
                if (hasRouteRole(to?.meta?.role as string[])) {
                    next();
                } else {
                    next("/403")
                }
            }
        } catch (error) {
            console.error(error)
            // 关闭sse连接
            await close()
            // 清空用户信息
            userStore.clearUserInfo()
            // 重定向到登录页面
            next({name: "Login"})
        }
    } else {
        // 重置主题
        themeStore.resetState();
        // 关闭sse连接
        await close()
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
