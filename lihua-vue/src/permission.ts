import router from "./router/index";
import NProgress from "nprogress"
import 'nprogress/nprogress.css'
import { useUserStore } from "@/stores/modules/user"
import { useThemeStore } from "@/stores/modules/theme";
import token from "@/utils/Token.ts"
import { init } from "@/utils/AppInit.ts";
import {hasRouteRole} from "@/utils/Auth.ts";
import {close, connect} from "@/utils/ServerSentEvents.ts";
import {message} from "ant-design-vue";
import Token from "@/utils/Token.ts";
const { getToken } = token
NProgress.configure({
    showSpinner: true
})

// 路由前置守卫
router.beforeEach(async (to, from, next) => {
    const userStore = useUserStore();
    const themeStore = useThemeStore();
    NProgress.start();

    const hasToken = getToken();

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
                    const loginSettingResp = await userStore.checkLoginSetting()
                    if (loginSettingResp.code === 200) {
                        if (loginSettingResp.data !== null) {
                            next({ name: 'Login', state: {data: loginSettingResp.data} });
                        }
                    } else {
                        message.error(loginSettingResp.msg)
                    }
                }
                // 判断用户是否拥有静态路由中指定的角色
                if (hasRouteRole(to?.meta?.role as string[])) {
                    // 已登录状态下，请求登陆页面自动跳转到首页
                    to.path === "/login" ? next('/index') : next({ ...to, replace: true })
                } else {
                    next("/403");
                }
            } else {
                // 连接到sse
                await connect()
                if (hasRouteRole(to?.meta?.role as string[])) {
                    next();
                } else {
                    next("/403");
                }
            }
        } catch (error) {
            console.error(error);
            userStore.clearUserInfo();
            await close()
            next("/login");
        }
    } else {
        // token 失效后重置主题后重定向到登陆页
        themeStore.resetState();
        await close()
        if (to.path === "/407") {
            next()
        } else {
            to.path !== "/login" ? next("/login") : next()
        }

    }

    NProgress.done();
});

// 路由后置守卫
router.afterEach((to,from) => { NProgress.done() })
