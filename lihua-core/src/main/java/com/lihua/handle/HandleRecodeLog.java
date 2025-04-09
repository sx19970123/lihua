package com.lihua.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.model.system.vo.SysLogVO;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.service.system.log.SysLogService;
import com.lihua.utils.date.DateUtils;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private static final Pattern CODE_PATTERN = Pattern.compile("\"code\":(\\d+)");
    private static final Pattern MSG_PATTERN = Pattern.compile("\"msg\":\"([^\"]*?)\"");
    private static final Pattern DATA_PATTERN = Pattern.compile("\"data\":\"([^\"]*?)\"");

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
                .setCreateTime(DateUtils.now())
                .setUrl(requestURI)
                .setUserAgent(userAgent)
                .setExecuteTime(time)
                .setDelFlag("0")
                .setExecuteStatus("0");

        // 处理返回值
        String result = handleResult(resultObject);

        // 返回值
        if (logAnnotation.recordResult()) {
            sysLogVO.setResult(result);
        }

        // 执行异常
        if (exception != null) {
            sysLogVO.setErrorMsg(exception.getMessage())
                    .setErrorStack(Arrays.toString(exception.getStackTrace()))
                    .setExecuteStatus("1");
        } else {
            // 使用正则表达式来获取执行状态
            if (result != null) {
                Matcher codeMatcher = CODE_PATTERN.matcher(result);
                // result 返回code值不为200即为执行失败，再通过正则表达式获取失败msg
                if (codeMatcher.find() && !"200".equals(codeMatcher.group(1))) {
                    Matcher msgMatcher = MSG_PATTERN.matcher(result);
                    if (msgMatcher.find()) {
                        sysLogVO.setErrorMsg(msgMatcher.group(1))
                                .setErrorStack("由业务判断返回ERROR，无堆栈信息")
                                .setExecuteStatus("1");
                    }

                }
            }
        }

        // 日志插入数据库
        // 登录日志单独保存
        if ("LOGIN".equals(type.getCode())) {
            // 从登录成功的返回值中获取token，拿到用户信息
            if (result != null && "0".equals(sysLogVO.getExecuteStatus())) {
                Matcher matcher = DATA_PATTERN.matcher(result);
                if (matcher.find()) {
                    LoginUser loginUser = LoginUserManager.getLoginUser(matcher.group(1));
                    if (loginUser != null) {
                        sysLogVO.setCreateId(loginUser.getUser().getId());
                        sysLogVO.setCreateName(loginUser.getUser().getNickname());
                        sysLogVO.setCacheKey(loginUser.getCacheKey());
                    }
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

    /**
     * 处理返回值
     */
    private String handleResult(Object resultObject) {
        if (resultObject == null) {
            return null;
        }

        if (resultObject instanceof String stringResult) {
            return stringResult;
        }

        if (resultObject instanceof ApiResponseModel<?> apiResponse) {
            try {
                return JsonUtils.toJsonIgnoreNulls(apiResponse);
            } catch (JsonProcessingException e) {
                return null;
            }
        }

        return resultObject.getClass().getName();
    }
}
