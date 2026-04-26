/// <reference types="vite/client"/>

// 环境变量
interface ImportMetaEnv {
    readonly VITE_APP_BASE_API: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}

// vue组件
declare module '*.vue' {
  import { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module 'uuid';

declare module 'lodash-es';

declare module 'wxmp-rsa';

declare module 'crypto-js';




