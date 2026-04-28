package com.lihua.log.aspect;

import com.lihua.common.utils.date.DateUtils;
import com.lihua.ip.utils.IpUtils;
import com.lihua.log.annotation.Log;
import com.lihua.log.handle.HandleRecodeLog;
import com.lihua.web.utils.WebUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;


/**
 * 处理系统日志
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Resource
    private HandleRecodeLog handleRecodeLog;

    /**
     * 环绕通知，方法执行完成后记录日志
     */
    @SneakyThrows
    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Log log) {
        // 当前请求 httpServletRequest
        HttpServletRequest httpServletRequest = WebUtils.getCurrentRequest();
        // 记录开始时间
        LocalDateTime startTime = DateUtils.now();
        // 记录返回值和异常
        Object proceed = null;
        Throwable exception = null;
        // 执行方法
        try {
            proceed = proceedingJoinPoint.proceed();
            return proceed;
        } catch (Throwable throwable) {
            exception = throwable;
            throw throwable;
        } finally {
            // 请求地址/用户关键字
            String requestURI = httpServletRequest == null ? "" : httpServletRequest.getRequestURI();
            String userAgent = httpServletRequest == null ? "" : httpServletRequest.getHeader("User-Agent");

            // 处理记录log
            handleRecodeLog.handleRecordLog(proceedingJoinPoint,
                    log,
                    Duration.between(startTime, DateUtils.now()).toMillis(),
                    proceed,
                    requestURI,
                    userAgent,
                    IpUtils.getIpAddress(),
                    WebUtils.getClientType(),
                    exception);
        }
    }
}
