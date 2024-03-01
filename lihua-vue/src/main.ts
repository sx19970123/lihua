import { createApp } from 'vue'
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
import "@/assets/css/index.css"
const icons:Record<string, Component> = Icons

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antd)

directive(app)

for (const i in icons) {
    app.component(i,icons[i])
}


app.mount('#app')
