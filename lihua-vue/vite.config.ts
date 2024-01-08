import { fileURLToPath, URL } from 'node:url'
import path from 'path'
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
      '@': path.resolve(__dirname,'./src')
    }
  },
  server: {
    port: 90,
    host: true,
    open: true,
    proxy: {
      '/dev-api': {
        target: 'http://localhost8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/dev-api/,'')
      }
    }
  }
})
