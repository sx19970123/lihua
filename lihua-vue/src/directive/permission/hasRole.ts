import {type App} from 'vue'
import {useUserStore} from "@/stores/modules/user.ts";

export default (app:App<Element>):void => {
  app.directive('hasRole',{
    created: (el, binding, vnode, prevVNode) => {
      const useUser = useUserStore()
      const roles = useUser.$state.roles
      const value = binding.value
      if (Array.isArray(value)) {
        if (roles && value) {
          for (const item of value as string[]) {
            if (!roles.includes(item)) {
              el.style.display = 'none'
              break;
            }
          }
        }
      } else {
        console.error('v-hasRole 指令中请传入字符串数组 如： v-hasRole="[\'xxx\']"')
      }
    }
  })
}
