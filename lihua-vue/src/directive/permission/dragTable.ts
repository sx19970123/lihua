import {type App} from 'vue'
import SortTable from "sortablejs"

/**
 * 针对 a-table 的表格拖拽排序
 * 指令：v-dragTable
 * 参数：表格数据对象集合
 * @param app
 */
export default (app:App) => {
    app.directive('dragTable',{
        updated: (el, binding, vnode) => {
            const value = binding.value.data.value
            const tbody = document.querySelector('.ant-table-tbody')
            if (tbody) {
                SortTable.create(tbody,{
                    draggable: '.ant-table-row',
                    sort: true,
                    animation: 150,
                    handle: ".drag-item",
                    ghostClass: "sortable-ghost",
                    chosenClass: "sortable-chosen",
                    dragClass: "sortable-drag",
                    onEnd: ({newIndex, oldIndex}: {newIndex:number,oldIndex: number}) => {
                        if (newIndex !== oldIndex) {
                            // 移动数组元素到新的位置
                            const item = value.splice(oldIndex, 1)[0]; // 移除旧位置的元素
                            value.splice(newIndex, 0, item); // 将元素插入到新位置
                            binding.value.fun(value)
                        }
                    }
                })
            }
        }
    })
}