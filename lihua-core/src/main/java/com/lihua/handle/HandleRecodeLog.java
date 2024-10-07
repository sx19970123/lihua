package com.lihua.handle;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.system.model.vo.SysLogVO;
import com.lihua.system.service.SysLogService;
import com.lihua.utils.json.JsonUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.LoginUserManager;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 处理日志对象的构建及调用对应 service保存至数据库
 */
@Component
public class HandleRecodeLog {

    // 登录日志service
    @Resource(name = "sysLoginLogService")
    private SysLogService sysLoginLogService;

    // 操作日志service
    @Resource(name = "sysOperateLogService")
    private SysLogService sysOperateLogService;

    /**
     * 通过传入参数整理组合为Log对象存入数据库
     * @param joinPoint 连接点获取参数等信息
     * @param logAnnotation log 注解，获取定义信息
     * @param time 业务执行时间
     * @param resultObject 业务返回值
     * @param exception 执行失败抛出异常
     */
    @Async
    public void handleRecordLog(JoinPoint joinPoint,
                                Log logAnnotation,
                                Long time,
                                Object resultObject,
                                String requestURI,
                                String userAgent,
                                String ip,
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
        if (logAnnotation.recordResult()) {
            String result = (resultObject instanceof String) ?
                    (String) resultObject :
                    (resultObject != null) ? resultObject.getClass().getName() : null;
            sysLogVO.setResult(result);
        }

        // 执行异常
        if (exception != null) {
            sysLogVO.setErrorMsg(exception.getMessage())
                    .setErrorStack(Arrays.toString(exception.getStackTrace()))
                    .setExecuteStatus("1");
        }

        // 日志插入数据库
        // 登录日志单独保存
        if ("LOGIN".equals(type.getCode())) {
            // 从登录成功的返回值中获取token，拿到用户信息
            if (resultObject != null) {
                Map<String, String> loginSuccessResultMap = JsonUtils.toObject(resultObject.toString(), Map.class);
                String token = loginSuccessResultMap.get("data");
                LoginUser loginUser = LoginUserManager.getLoginUser(token);
                if (loginUser != null) {
                    sysLogVO.setCreateId(loginUser.getUser().getId());
                    sysLogVO.setCreateName(loginUser.getUser().getNickname());
                    sysLogVO.setCacheKey(loginUser.getCacheKey());
                }
            }
            // 登录参数中获取 username
            List<Object> currentUserList = Stream.of(joinPoint.getArgs())
                    .filter(arg -> arg instanceof CurrentUser)
                    .toList();
            if (!currentUserList.isEmpty()) {
                CurrentUser currentUser = (CurrentUser) currentUserList.get(0);
                sysLogVO.setUsername(currentUser.getUsername());
            }

            sysLoginLogService.insert(sysLogVO);
        } else {
            CurrentUser user = LoginUserContext.getUser();
            if (StringUtils.hasText(user.getId())) {
                sysLogVO.setCreateId(user.getId());
                sysLogVO.setCreateName(user.getNickname());
                sysLogVO.setUsername(user.getUsername());
                sysLogVO.setCacheKey(LoginUserContext.getLoginUser().getCacheKey());
            }
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
            String name = parameters[i].getName();
            // 当排除字段中包当前字段，则跳过循环
            if (Arrays.asList(excludeParams).contains(name)) {
                continue;
            }
            // 将参数转为json，无法转换的转为CanonicalName
            String jsonOrCanonicalName = JsonUtils.toJsonOrCanonicalName(args[i]);
            // 排除特定字段后的json字符串
            String excludeJson = JsonUtils.excludeJsonKey(jsonOrCanonicalName, Arrays.asList(excludeParams));
            resultParamMap.put(name, excludeJson);
        }

        return JsonUtils.toJsonOrCanonicalName(resultParamMap);
    }

}
