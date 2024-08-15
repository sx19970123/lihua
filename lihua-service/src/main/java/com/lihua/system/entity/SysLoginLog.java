package com.lihua.system.entity;

import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import com.lihua.system.model.vo.SysLogVO;

/**
 * 系统登录日志
 * 与操作日志相同，存在不同名的表中
 */
@ExcelModel(sheetName = "登录日志",includeAllField = false)
public class SysLoginLog extends SysLogVO {

}
