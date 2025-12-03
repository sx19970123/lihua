import {fileURLToPath, URL} from 'node:url'
import {defineConfig, loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'
import svgLoader from 'vite-svg-loader'
import replaceAttrFill from "./plugins/SvgoPlugin.ts"

export default defineConfig(({ mode }) => {
  // 获取请求前缀
  const env = loadEnv(mode, process.cwd());
  const baseApi = env.VITE_APP_BASE_API
  const wsBaseApi = env.VITE_APP_WS_API
  return {
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
        },
        [wsBaseApi]: {
          target: 'ws://localhost:8080',
          changeOrigin: true,
          ws: true
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
    plugins: [
      vue(),
      // 将svg转为vue组件
      svgLoader({
        svgo: true,
        svgoConfig: {
          plugins: [
            // 设置svg图标fill属性为currentColor，保证图标受css控制改变颜色
            // 参数为排除目录，保证该目录下的图标不受影响，防止彩色图标被修改
            replaceAttrFill("assets/icons/fixed-color"),
            // 删除图标的width和height属性
            {
              name: "removeAttrs",
              params: {
                attrs: ['width', 'height'],
              }
            },
              // 设置图标的width和height属性为1em
            {
              name: "addAttributesToSVGElement",
              params: {
                attributes: [
                  {'width': '1em'},
                  {'height': '1em'},
                ]
              }
            }
          ]
        }
      })
    ],
  }
})