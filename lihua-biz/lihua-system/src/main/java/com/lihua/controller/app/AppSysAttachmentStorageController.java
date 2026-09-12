package com.lihua.controller.app;

import com.lihua.controller.BaseSysAttachmentStorageController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "APP-附件存储")
@RestController
@RequestMapping("app/system/attachment/storage")
public class AppSysAttachmentStorageController extends BaseSysAttachmentStorageController {

}
