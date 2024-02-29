import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
  ],
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
      '/dev-api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (p:string) => p.replace(/^\/dev-api/, '')
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
})
