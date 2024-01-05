package com.lihua.model;

import com.lihua.enums.AxiosResultCodeEnum;
import com.lihua.utils.json.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import static com.lihua.enums.AxiosResultCodeEnum.SUCCESS;

@Data
@AllArgsConstructor
public class AxiosResult  {

    
    public static <T> String success() {
        return success(null);
    }

    public static <T> String success(T data) {
        return success(SUCCESS.getDefaultMsg(), data);
    }

    public static <T> String success(String msg, T data) {
        return response(SUCCESS, msg, data);
    }

    public static <T> String error(AxiosResultCodeEnum resultCodeEnum,String message) {
        return error(resultCodeEnum, message, null);
    }

    public static <T> String error(AxiosResultCodeEnum resultCodeEnum) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), null);
    }
    
    public static <T> String error(AxiosResultCodeEnum resultCodeEnum, T data) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }
    
    public static <T> String error(AxiosResultCodeEnum resultCodeEnum,String msg, T data) {
        return response(resultCodeEnum, msg, data);
    }


    private static <T> String response(AxiosResultCodeEnum resultCodeEnum,String msg,T data) {
        return JsonUtils.toJson(Wrap.wrap(resultCodeEnum, msg, data));
    }

}
