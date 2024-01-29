import {createApp} from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './permission'
// antd
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
// andv 图标
import * as Icons from "@ant-design/icons-vue";
import "@/assets/custom.css"

const icons = Icons

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antd)

for (const i in icons) {
    app.component(i,(Icons as any)[i])
}


app.mount('#app')
