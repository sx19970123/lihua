import { type App } from 'vue';

export default (app: App<Element>): void => {
    app.directive('rollDisable', {
        mounted(el, binding) {
            // 传入参数为false时，不进行鼠标滚轮禁用
            if (binding.value) {
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

            }

        },
        unmounted(el) {
            // 确保在组件卸载时移除事件监听器
            if (el._onDestroy) {
                el._onDestroy();
            }
        },
    });
};
