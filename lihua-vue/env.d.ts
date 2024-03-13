/// <reference types="vite/client" />
declare module '*.vue' {
    import type { DefineComponent } from 'vue'
    import Vue from 'vue'

    const component: DefineComponent<{}, {}, any> | Vue

    export default component
}


interface ImportMetaEnv {
    readonly VITE_APP_BASE_API: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}

declare module 'js-cookie'

declare module 'nprogress'

declare module 'crypto-js'

declare module '@/components/verifition/index.vue'

declare module 'vue-cropper'

declare module 'vue-draggable-plus'

declare module 'lodash-es'
