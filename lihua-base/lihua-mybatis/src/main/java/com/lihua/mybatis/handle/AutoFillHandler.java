package com.lihua.mybatis.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.security.manager.LoginUserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mybatis plus 元数据自动填充
 */
@Component
public class AutoFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建用户
        if (LoginUserContext.getUserId() != null) {
            this.strictInsertFill(metaObject, "createId", String.class, LoginUserContext.getUserId());
        }
        // 创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, DateUtils.now());
        // 逻辑删除
        this.strictInsertFill(metaObject, "delFlag", String.class, "0");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新用户
        if (LoginUserContext.getUserId() != null) {
            this.strictUpdateFill(metaObject, "updateId", String.class, LoginUserContext.getUserId());
        }
        // 更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, DateUtils.now());
    }
}
