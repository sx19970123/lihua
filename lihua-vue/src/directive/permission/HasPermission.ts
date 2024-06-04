import { type App } from 'vue'
import { useUserStore } from "@/stores/modules/user.ts";
import { cloneDeep, isEqual } from 'lodash-es'
const userStore = useUserStore()
export default (app:App<Element>):void => {
  app.directive('hasPermission',{
    mounted: (el, binding) => {
      const currentPerms = cloneDeep(userStore.$state.permissions)
      const value = binding.value
      if (Array.isArray(value)) {
        if (value) {
          for (const permission of value as string[]) {
            if (!currentPerms.includes(permission)) {
              el.remove()
              break
            }
          }
        }
      } else {
        console.error('v-hasPermission 指令: 参数错误，请传入字符串数组，如 v-hasRole="[\'xxx:xxx:xxx\']"')
      }
    }
  })
}
