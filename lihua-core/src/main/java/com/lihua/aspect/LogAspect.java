package com.lihua.aspect;

import com.lihua.annotation.Log;
import com.lihua.handle.HandleRecodeLog;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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

    // 获取http请求
    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private HandleRecodeLog handleRecodeLog;


    /**
     * 环绕通知，方法执行完成后记录日志
     */
    @SneakyThrows
    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Log log) {
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
                httpServletRequest.getRemoteAddr(),
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
        handleRecodeLog.handleRecordLog(joinPoint,
                log,
                null,
                null,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getHeader("User-Agent"),
                httpServletRequest.getRemoteAddr(),
                exception);
    }

}
