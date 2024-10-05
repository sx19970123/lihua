package com.lihua.aspect;

import com.lihua.annotation.Log;
import com.lihua.handle.HandleRecodeLog;

import com.lihua.utils.web.WebUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        LocalDateTime startTime = LocalDateTime.now();
        // 执行方法
        Object proceed = proceedingJoinPoint.proceed();
        // 处理记录log
        handleRecodeLog.handleRecordLog(proceedingJoinPoint,
                log,
                Duration.between(startTime, LocalDateTime.now()).toMillis(),
                proceed,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getHeader("User-Agent"),
                getClientIp(),
                null);

        return proceed;
    }

    /**
     * 异常通知，当执行方法发生异常时记录日志
     * @param joinPoint
     * @param log
     */
    @AfterThrowing(pointcut = "@annotation(log)", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Log log, Throwable exception) {
        // 当前请求 httpServletRequest
        HttpServletRequest httpServletRequest = WebUtils.getCurrentRequest();
        handleRecodeLog.handleRecordLog(joinPoint,
                log,
                null,
                null,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getHeader("User-Agent"),
                getClientIp(),
                exception);
    }


    // 获取客户端ip
    private String getClientIp() {
        HttpServletRequest request = WebUtils.getCurrentRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
