import request from "@/utils/request";

export const getMenuInfo = () => {
    return request({
        headers: {
          token: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxIn0.knD0DKXfe3ZCDlHIOIeNHbiwlkF_1KKxAPD85KW0a4A'
        },
        url: 'test',
        method: 'get'
    })
}
