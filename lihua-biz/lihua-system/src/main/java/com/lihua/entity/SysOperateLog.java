package com.lihua.entity;

import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import com.lihua.model.vo.SysLogVO;


/**
 * 系统操作日志
 */
@ExcelModel(sheetName = "操作日志",includeAllField = false)
public class SysOperateLog extends SysLogVO {

}
