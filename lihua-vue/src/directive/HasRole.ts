import { type App } from 'vue'
import { useUserStore } from "@/stores/user.ts";
import { cloneDeep } from 'lodash-es';
const userStore = useUserStore()

export default function setupHasRoleDirective(app: App<Element>): void {
  app.directive('hasRole', {
    mounted (el, binding) {
      const currentRoles = cloneDeep(userStore.$state.roleCodes)
      const value = binding.value
      if (Array.isArray(value)) {
        if (currentRoles && value) {
          for (const role of value as string[]) {
            if (!currentRoles.includes(role)) {
              el.remove()
              break
            }
          }
        }
      } else {
        console.error('v-hasRole 指令: 请提供一个字符串数组，例 v-hasRole="[\'xxx\']"')
      }
    },
  })
}
