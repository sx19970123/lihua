package com.lihua.web.handle;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.ResponseModel;
import com.lihua.common.model.response.Responses;
import com.lihua.web.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 围栏层响应端点：未经 MVC 管线的组件（security filter 等）经 WebUtils.renderResponse 转入此处，
 * 由消息转换管线完成内容协商（/error 的 error dispatch 同模式，为携带 200+body code 载荷改用显式 forward）
 */
@RestController
public class FenceResponseController {

    @RequestMapping(WebUtils.FENCE_PATH)
    public ResponseModel<?> response(HttpServletRequest request) {
        ResponseModel<?> model = (ResponseModel<?>) request.getAttribute(WebUtils.FENCE_MODEL_ATTR);
        // 非围栏转入的直接访问，按全局 404 同款响应
        return model != null ? model : Responses.error(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR);
    }
}
