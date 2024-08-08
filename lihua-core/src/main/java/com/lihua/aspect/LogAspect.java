package com.lihua.aspect;

import com.lihua.annotation.Log;
import com.lihua.system.service.SysLogService;
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

    // 获取http请求
    @Resource
    private HttpServletRequest request;

    // 登录日志service
    @Resource(name = "sysLoginLogService")
    private SysLogService sysLoginLogService;

    // 操作日志service
    @Resource(name = "sysOperateLogService")
    private SysLogService sysOperateLogService;

    /**
     * 后置通知，方法执行完成后将操作记录到数据库
     */
    @SneakyThrows
    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Log log) {
        // 记录开始时间
        LocalDateTime startTime = LocalDateTime.now();
        // 执行方法
        Object proceed = proceedingJoinPoint.proceed();
        // 方法执行的毫秒数
        long millis = Duration.between(startTime, LocalDateTime.now()).toMillis();

        handleLog(proceedingJoinPoint);

        if (proceed instanceof String) {
            System.out.println("返回值" + proceed);
        } else {
            System.out.println("返回对象" + proceed.getClass().getName());
        }



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
        // 所有参数
        Object[] args = joinPoint.getArgs();
        // 执行方法
        Signature signature = joinPoint.getSignature();
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


    }

}
