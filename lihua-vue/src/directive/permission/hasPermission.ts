import {type App} from 'vue'
import {useUserStore} from "@/stores/modules/user.ts";

export default (app:App<Element>):void => {
    app.directive('hasPermission',{
        created: (el, binding, vnode) => {
            const useUser = useUserStore()
            const perms = useUser.$state.permissions
            const value = binding.value
            if (Array.isArray(value)) {
                if (perms && value as string[]) {
                    for (const item of value) {
                        if (!perms.includes(item)) {
                            el.style.display = 'none'
                            break;
                        }
                    }
                }
            } else {
                console.error('v-hasPermission 指令中请传入字符串数组 如： v-hasPermission="[\'xxx:xxx:xxx\']"')
            }

        }
    })
}
