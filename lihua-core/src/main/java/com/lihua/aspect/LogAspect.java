package com.lihua.aspect;

import com.lihua.annotation.Log;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;


/**
 * 处理系统日志切面
 */
@Aspect
@Component
public class LogAspect {

    @Resource
    private HttpServletRequest request;


    /**
     * 后置通知，方法执行完成后将操作记录到数据库
     */
    @SneakyThrows
    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Log log) {
        LocalDateTime startTime = LocalDateTime.now();
        // 所有参数
        Object[] args = proceedingJoinPoint.getArgs();
        // 执行方法
        Signature signature = proceedingJoinPoint.getSignature();
        // 方法名
        String name = signature.getName();
        // 全限定类名
        String declaringTypeName = signature.getDeclaringTypeName();
        // 请求地址
        String requestURI = request.getRequestURI();

        // 浏览器等信息
        String header = request.getHeader("User-Agent");

        // ip地址
        String ip = request.getRemoteAddr();

        // String userId = LoginUserContext.getUserId();

        LocalDateTime now = LocalDateTime.now();

        // 返回结果
        Object proceed = null;

        proceed = proceedingJoinPoint.proceed();
        handleLog(proceedingJoinPoint);


        LocalDateTime endTime = LocalDateTime.now();

        Duration between = Duration.between(startTime, endTime);

        // 执行毫秒数
        long millis = between.toMillis();
        System.out.println(millis);
        System.out.println(proceedingJoinPoint);
        System.out.println(log);
        System.out.println("后置通知");

        return proceed;
    }

    @AfterThrowing("@annotation(log)")
    public void afterThrowing(JoinPoint joinPoint, Log log) {
        System.out.println("异常通知");
        handleLog(joinPoint);
    }

    private void handleLog(JoinPoint joinPoint) {

    }

}
