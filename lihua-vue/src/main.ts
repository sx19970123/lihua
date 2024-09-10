import {createApp, defineComponent} from 'vue'
import type { Component } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './permission'
import directive from './directive'

// antd
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
// andv 图标
import * as Icons from "@ant-design/icons-vue";
import "@/static/css/index.css"

import Icon from "@/components/icon/index.vue"


const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antd)
// 指令
directive(app)
// ant 自带图标
const icons:Record<string, Component> = Icons
for (const i in icons) {
    app.component(i,icons[i])
}

// 导入自定义图标
const modules = import.meta.glob("./components/icon/**/*.vue")
for (let path in modules) {
    modules[path]().then((module:any) => {
        if (module && module.default) {
            // 组件名
            const match = path.match(/\/([^/]+)\.vue$/)
            if (match) {
                // 注册组件
                app.component(match[1], defineComponent(module.default));
            }
        }
    });
}

// 导入登录后配置组件
const loginSettingComponents = import.meta.glob("./components/login-setting/*/*.vue")
for (const path in loginSettingComponents) {
    loginSettingComponents[path]().then((module: any) => {
        if (module && module.default) {
            // 组件名
            const match = path.match(/\/([^/]+)\.vue$/)
            if (match) {
                // 注册组件
                app.component(match[1], defineComponent(module.default));
            }
        }
    })
}

// 是用全局自定义icon组件
app.component('icon', Icon)

app.mount('#app')
