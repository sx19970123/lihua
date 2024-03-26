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
// 自定义图标默认放在/static/icon 下
const modules = import.meta.glob("./static/icon/**/*.js")
const icons:Record<string, Component> = Icons

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antd)
// 指令
directive(app)
// ant 自带图标
for (const i in icons) {
    app.component(i,icons[i])
}
for (let path in modules) {
    modules[path]().then(module => {
        // 从路径中提取文件名
        const fileName = path.split('/').pop()!.replace(/\.\w+$/, '');
        // 使用 defineComponent 包装组件
        const component = defineComponent(module.default);
        // 注册组件，使用文件名作为组件名
        app.component(fileName, component);
    });
}

app.mount('#app')
