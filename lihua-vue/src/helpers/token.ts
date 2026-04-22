import {useStorage} from "@vueuse/core";

const TOKEN_KEY: string = "lihua_token"
const tokenStorage = useStorage<null | string>(TOKEN_KEY, null);

// token
const getToken = ():string => {
    return tokenStorage.value || ''
}

const setToken = (token: string):void => {
    tokenStorage.value = token
}

const removeToken = () => {
    tokenStorage.value = null
}

export default {
    getToken,
    setToken,
    removeToken
}
