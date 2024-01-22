import { defineStore } from "pinia";
import { useUserStore } from "@/stores/modules/user"

export const usePermissionStore = defineStore('permission',{
    state: ()=> {
        return {
            // 可在侧边栏显示的路由
            sidebarRouters: [],
            // 可在顶栏显示的路由
            viewTabsRouters: []
        }
    },
    actions: {
        // 初始化路由信息
        initRouters(routerList:any):void {
            this.$state.sidebarRouters = routerList
            this.$state.sidebarRouters = routerList
        }
    },
    getters: {

    }
})
