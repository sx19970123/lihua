package com.lihua.controller.app;

import com.lihua.controller.base.BaseSysDictDataController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "APP-字典数据")
@RestController
@RequestMapping("app/system/dictData")
public class AppSysDictDataController extends BaseSysDictDataController {

}
