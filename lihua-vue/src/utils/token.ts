import Cookies from "js-cookie"

const TOKEN_KEY: string = "token"

const getToken = ():string => {
    return Cookies.get(TOKEN_KEY);
}

const setToken = (token: string):void => {
    Cookies.set(TOKEN_KEY,token)
}

const removeToken = () => {
    Cookies.remove(TOKEN_KEY)
}

export default {
    getToken,
    setToken,
    removeToken
}
