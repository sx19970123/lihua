import { type App } from 'vue'
import { useUserStore } from "@/stores/modules/user.ts";
import { cloneDeep } from 'lodash-es';
const userStore = useUserStore()

export default function setupHasRoleDirective(app: App<Element>): void {
  app.directive('hasRole', {
    created (el, binding) {
      const currentRoles = cloneDeep(userStore.$state.roles)
      const value = binding.value
      if (Array.isArray(value)) {
        if (currentRoles && value) {
          let hasRole = true

          for (const role of value as string[]) {
            if (!currentRoles.includes(role)) {
              hasRole = false
              break
            }
          }
          el.style.display = hasRole ? '' : 'none'
        }
      } else {
        console.error('v-hasRole 指令: 请提供一个字符串数组，例如 v-hasRole="[\'xxx\']"')
      }
    }
  })
}
