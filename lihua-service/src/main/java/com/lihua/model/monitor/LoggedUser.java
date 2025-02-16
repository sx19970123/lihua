package com.lihua.model.monitor;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LoggedUser implements Serializable {

    /**
     * 缓存 key
     */
    private String cacheKey;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户ip
     */
    private String ip;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

}
