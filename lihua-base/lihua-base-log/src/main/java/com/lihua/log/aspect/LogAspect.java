package com.lihua.log.aspect;

import com.lihua.common.utils.date.DateUtils;
import com.lihua.common.utils.web.WebUtils;
import com.lihua.ip.utils.IpUtils;
import com.lihua.log.annotation.Log;
import com.lihua.log.handle.HandleRecodeLog;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
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
        // 执行方法
        Object proceed = proceedingJoinPoint.proceed();
        // 处理记录log
        handleRecodeLog.handleRecordLog(proceedingJoinPoint,
                log,
                Duration.between(startTime, DateUtils.now()).toMillis(),
                proceed,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getHeader("User-Agent"),
                IpUtils.getIpAddress(),
                WebUtils.getClientType(),
                null);

        return proceed;
    }

    /**
     * 异常通知，当执行方法发生异常时记录日志
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
                IpUtils.getIpAddress(),
                WebUtils.getClientType(),
                exception);
    }
}
