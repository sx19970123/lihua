import {useStorage} from "@vueuse/core";

const POST_LOGIN_CHECK_KEY: string = "lihua_post_login_check_data"
const postLoginCheckStore = useStorage<string | null>(POST_LOGIN_CHECK_KEY, null);

/**
 * 设置需要检查的项
 */
const setData = (componentNames: string[]) => {
    postLoginCheckStore.value = componentNames.join(",")
}

/**
 * 获取需要检查的项
 */
const getData = (): string[] | null | undefined => {
    return postLoginCheckStore.value?.split(",")
}

/**
 * 清除需要检查的项
 */
const clearData = () => {
    postLoginCheckStore.value = null
}

/**
 * 删除单个元素
 */
const removeDataItem = (checkDataItem: string) => {
    const checkData = getData()
    if (checkData && checkData.length > 0) {
        const filter = checkData.filter(item => item !== checkDataItem)
        if (filter.length > 0) {
            setData(filter)
        } else {
            clearData()
        }
    }
}

export default {
    getData,
    setData,
    clearData,
    removeDataItem
}