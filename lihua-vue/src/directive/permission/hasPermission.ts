import { type App } from 'vue'
import { useUserStore } from "@/stores/modules/user.ts";
import { cloneDeep, isEqual } from 'lodash-es'
const useUser = useUserStore()
let cachedPerms: string[] = []
export default (app:App<Element>):void => {
  app.directive('hasPermission',{
    beforeUpdate: (el, binding) => {
      const currentPerms = cloneDeep(useUser.$state.permissions)
      // 每次调用对比 currentPerms、cachedPerms是否相同，不相同再执行处理dom元素
      if (!isEqual(currentPerms,cachedPerms)) {
        cachedPerms = currentPerms
        const value = binding.value
        if (Array.isArray(value)) {
          if (cachedPerms && value) {
            let hasPermission = true

            for (const permission of value as string[]) {
              if (!cachedPerms.includes(permission)) {
                hasPermission = false
                break
              }
            }

            el.style.display = hasPermission ? '' : 'none'
          }
        } else {
          console.error('v-hasPermission 指令: 参数错误，请传入字符串数组，如 v-hasRole="[\'xxx:xxx:xxx\']"')
        }
      }
    }
  })
}
