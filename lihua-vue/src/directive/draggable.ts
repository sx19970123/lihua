import {type App, type DirectiveBinding, ref, watch, watchEffect} from 'vue'
import {useDraggable} from '@vueuse/core';
import {isMobile} from 'is-mobile'

export default (app: App<Element>) => {
    app.directive('draggable', {
        mounted(el: HTMLElement, binding: DirectiveBinding) {
            // 移动端不加载拖拽
            if (isMobile()) {
                return
            }
            // 绑定的css
            let targetClass = binding.value
            if (!targetClass) {
                targetClass = '.ant-modal-content'
            }

            el.style.cursor = 'move'
            // 当前拖拽目标
            const target = ref(el)
            const { x, y, isDragging } = useDraggable(target)

            // 拖拽相关状态
            const startX = ref(0)
            const startY = ref(0)
            const startedDrag = ref(false)
            const transformX = ref(0)
            const transformY = ref(0)
            const preTransformX = ref(0)
            const preTransformY = ref(0)
            const dragRect = ref({ left: 0, right: 0, top: 0, bottom: 0 })

            // 监听位置变化
            watch([x, y], () => {
                if (!startedDrag.value) {
                    startX.value = x.value
                    startY.value = y.value

                    const bodyRect = document.body.getBoundingClientRect()
                    const titleRect = el.getBoundingClientRect()

                    dragRect.value.right = bodyRect.width - titleRect.width
                    dragRect.value.bottom = bodyRect.height - titleRect.height

                    preTransformX.value = transformX.value
                    preTransformY.value = transformY.value
                }
                startedDrag.value = true
            })

            // 拖动状态变化
            watch(isDragging, () => {
                if (!isDragging.value) {
                    startedDrag.value = false
                }
            })
            // 拖拽计算
            watchEffect(() => {
                if (startedDrag.value) {
                    // 推拽位置计算
                    transformX.value = preTransformX.value + Math.min(Math.max(dragRect.value.left + 24, x.value), dragRect.value.right - 24) - startX.value
                    transformY.value = preTransformY.value + Math.min(Math.max(dragRect.value.top, y.value), dragRect.value.bottom) - startY.value

                    // 绑定 transform 到 modal 容器
                    const modalWrap = el.closest(targetClass)

                    if (modalWrap) {
                        (modalWrap as HTMLElement).style.transform = `translate(${transformX.value}px, ${transformY.value}px)`
                    } else {
                        console.error(`没有找到可拖拽元素class，请在指令中传入正确参数，例：v-draggable="'.ant-modal-content'"，其中.ant-modal-content为默认值，如果需要为自己元素添加拖拽功能，请传入自定义class`)
                    }
                }
            })
        }
    })
}