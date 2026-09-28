package com.lihua.controller.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.model.dto.SysNoticeDTO;
import com.lihua.model.vo.SysNoticeVO;
import com.lihua.model.vo.SysUserNoticeVO;
import com.lihua.service.SysNoticeService;
import com.lihua.service.SysUserNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 通知公告控制器基类：承载管理版与 App 版共有端点——用户侧的预览、列表、标星、已读与未读计数
 * （对外行为双版本一致，差异仅在路由前缀与文档分组）；公告的维护端点（分页/详情/保存/发布/撤销/删除/已读统计）
 * 仅管理版提供，由管理版子类自行声明
 */
public abstract class BaseSysNoticeController extends ApiResponseController {

    @Resource
    protected SysNoticeService sysNoticeService;

    @Resource
    protected SysUserNoticeService sysUserNoticeService;

    @Operation(summary = "根据id预览公告")
    @GetMapping("preview/{id}")
    public ApiResponseModel<SysNoticeVO> preview(@PathVariable("id") String id) {
        return success(sysNoticeService.preview(id));
    }

    @Operation(summary = "用户查询自己的通知公告")
    @PostMapping("list")
    public ApiResponseModel<IPage<SysUserNoticeVO>> userMessageList(@RequestBody SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.userMessageList(sysNoticeDTO));
    }

    @Operation(summary = "标星公告")
    @PostMapping("star/{noticeId}/{star}")
    public ApiResponseModel<String> changeStar(@PathVariable("noticeId") String noticeId, @PathVariable("star") String star) {
        sysUserNoticeService.changeStar(noticeId, star);
        return success();
    }

    @Operation(summary = "记录已读")
    @PostMapping("read/{noticeId}")
    public ApiResponseModel<String> changeRead(@PathVariable("noticeId") String noticeId) {
        sysUserNoticeService.changeRead(noticeId);
        return success();
    }

    @Operation(summary = "获取未读总数")
    @GetMapping("unread/count")
    public ApiResponseModel<Integer> queryUnReadCount() {
        return success(sysUserNoticeService.queryUnReadCount());
    }
}
