import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  // 获取请求前缀
  const env = loadEnv(mode, process.cwd());
  const baseApi = env.VITE_APP_BASE_API
  return {
    plugins: [vue()],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      },
    },
    server: {
      port: 90,
      host: true,
      open: true,
      proxy: {
        [baseApi]: {
          target: 'http://localhost:8080',
          changeOrigin: true,
          rewrite: (p:string) => p.replace(baseApi, '')
        }
      }
    },
    css: {
      preprocessorOptions: {
        less: {
          javascriptEnabled: true
        }
      }
    },
    build: {
      outDir: 'dist', // 输出目录
      target: 'esnext', // js格式
      terserOptions: {
        compress: {
          drop_console: true, // 生产环境去掉 console
          drop_debugger: true, // 生产环境去掉 debugger
          dead_code: true // 删除无法访问的代码
        },
      },
      chunkSizeWarningLimit: 2000 // 代码块超过2000 bytes时vite发出警告
    }
  }
})
