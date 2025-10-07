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
      outDir: 'dist',
      target: 'esnext',
      minify: 'esbuild',
      esbuild: {
        drop: ['console', 'debugger'],
      },
      chunkSizeWarningLimit: 2000,
    },
  }
})
