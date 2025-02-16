package com.lihua.service.monitor.server;

import com.lihua.model.monitor.ServerInfo;

public interface MonitorServerService {

    /**
     * 获取服务器运行数据
     */
    ServerInfo serverInfo();
}
