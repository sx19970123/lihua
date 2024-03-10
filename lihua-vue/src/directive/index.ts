import { type App } from 'vue'

// 导出全部指令
const modules = import.meta.glob('../directive/**/**.ts')
export default (app:App<Element>) => {
  for (const path in modules) {
    // 排除当前文件
    if(path !== '../directive/index.ts') {
      modules[path]().then((mod:any) => {
        if (mod && mod.default) {
          mod.default(app)
        }
      })
    }
  }
}
