import {createApp} from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './permission'
// antv
import Antv from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
// andv 图标
import * as Icons from "@ant-design/icons-vue";
const icons = Icons

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antv)

for (const i in icons) {
    app.component(i,icons[i])
}


app.mount('#app')
