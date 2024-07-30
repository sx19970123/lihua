import { type App } from 'vue';

export default (app: App<Element>): void => {
    app.directive('rollDisable', {
        mounted(el) {
            // 禁用滚轮事件的函数
            const preventScroll = (e: Event) => {
                e.preventDefault();
            };

            // 添加滚轮事件监听器
            el.addEventListener('wheel', preventScroll, { passive: false });

            // 在指令的 unmounted 钩子中移除事件监听器
            el._onDestroy = () => {
                el.removeEventListener('wheel', preventScroll);
            };
        },
        unmounted(el) {
            // 确保在组件卸载时移除事件监听器
            if (el._onDestroy) {
                el._onDestroy();
            }
        },
    });
};
