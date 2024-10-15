package com.lihua.monitor.service;

import com.lihua.monitor.model.ServerInfo;

public interface MonitorServerService {

    /**
     * 获取服务器运行数据
     */
    ServerInfo serverInfo();
}
