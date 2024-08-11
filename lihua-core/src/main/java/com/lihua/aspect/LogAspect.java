package com.lihua.aspect;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.system.model.vo.SysLogVO;
import com.lihua.system.service.SysLogService;
import com.lihua.utils.json.JsonUtils;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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

    // 登录日志service
    @Resource(name = "sysLoginLogService")
    private SysLogService sysLoginLogService;

    // 操作日志service
    @Resource(name = "sysOperateLogService")
    private SysLogService sysOperateLogService;

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
        handleRecordLog(proceedingJoinPoint,
                log,
                Duration.between(startTime, LocalDateTime.now()).toMillis(),
                proceed,
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
        handleRecordLog(joinPoint,
                log,
                null,
                null,
                exception);
    }

    /**
     * 通过传入参数整理组合为Log对象存入数据库
     * @param joinPoint 连接点获取参数等信息
     * @param logAnnotation log 注解，获取定义信息
     * @param time 业务执行时间
     * @param resultObject 业务返回值
     * @param exception 执行失败抛出异常
     */
    private void handleRecordLog(JoinPoint joinPoint,
                                 Log logAnnotation,
                                 Long time,
                                 Object resultObject,
                                 Throwable exception) {

        String description = logAnnotation.description();
        LogTypeEnum type = logAnnotation.type();
        // 接口参数
        String params = handleExcludeParams(joinPoint, logAnnotation.excludeParams());
        // 执行方法
        Signature signature = joinPoint.getSignature();
        // 方法名
        String name = signature.getName();
        // 全限定类名
        String declaringTypeName = signature.getDeclaringTypeName();
        // 请求地址
        String requestURI = httpServletRequest.getRequestURI();
        // 浏览器等信息
        String userAgent = httpServletRequest.getHeader("User-Agent");
        // ip地址
        String ip = httpServletRequest.getRemoteAddr();

        // 构建LogVO对象
        SysLogVO sysLogVO = new SysLogVO();
        sysLogVO.setDescription(description)
                .setTypeCode(type.getCode())
                .setTypeMsg(type.getMsg())
                .setClassName(declaringTypeName)
                .setMethodName(name)
                .setIpAddress(ip)
                .setParams(params)
                .setCreateTime(LocalDateTime.now())
                .setUrl(requestURI)
                .setUserAgent(userAgent)
                .setExecuteTime(time)
                .setDelFlag("0")
                .setExecuteStatus("0");

        // 返回值
        if (resultObject instanceof String) {
            sysLogVO.setResult(String.valueOf(resultObject));
        } else if (resultObject != null) {
            sysLogVO.setResult(resultObject.getClass().getName());
        }
        // 执行异常
        if (exception != null) {
            sysLogVO.setErrorMsg(exception.getMessage())
                    .setErrorStack(Arrays.toString(exception.getStackTrace()))
                    .setExecuteStatus("1");
        }

        try {
            // 无登录状态下获取用户id异常
            sysLogVO.setCreateId(LoginUserContext.getUserId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 日志插入数据库
        // 登录日志单独保存
        if ("LOGIN".equals(type.getCode())) {
            sysLoginLogService.insert(sysLogVO);
        } else {
            sysOperateLogService.insert(sysLogVO);
        }
    }

    /**
     * 处理排除参数
     * @param joinPoint 切点，获取参数名及参数
     * @param excludeParams 排除参数名称数组
     */
    private String handleExcludeParams(JoinPoint joinPoint, String[] excludeParams) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            return null;
        }

        // 所有参数名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();


        // 返回的map
        Map<String, String> resultParamMap = new HashMap<>();

        // 转为json
        for (int i = 0; i < parameters.length; i++) {
            // 将参数转为json，无法转换的转为CanonicalName
            String jsonOrCanonicalName = JsonUtils.toJsonOrCanonicalName(args[i]);
            // 排除特定字段后的json字符串
            String excludeJson = JsonUtils.excludeJsonKey(jsonOrCanonicalName, Arrays.asList(excludeParams));
            resultParamMap.put(parameters[i].getName(), excludeJson);
        }

        return JsonUtils.toJsonOrCanonicalName(resultParamMap);
    }

}
