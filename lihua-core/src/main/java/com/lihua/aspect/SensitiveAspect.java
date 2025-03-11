package com.lihua.aspect;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lihua.annotation.sensitive.ApplySensitive;
import com.lihua.annotation.sensitive.DeepSensitive;
import com.lihua.annotation.sensitive.Sensitive;
import com.lihua.exception.SensitiveException;
import com.lihua.utils.security.LoginUserContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class SensitiveAspect {

    private static final Cache<Class<?>, List<Field>> FIELD_CACHE = CacheBuilder.newBuilder().maximumSize(1000).build();


    @SneakyThrows
    @Around("@annotation(applySensitive)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, ApplySensitive applySensitive) {
        Object object = proceedingJoinPoint.proceed();

        if (object == null) {
            return null;
        }

        return process(object);
    }

    // 执行脱敏
    private Object process(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof IPage<?> iPage) {
            iPage.getRecords().forEach(this::process);                      // 处理分页数据
        } else if (object instanceof Collection<?> collection) {
            collection.forEach(this::process);                              // 处理集合数据
        } else if (object instanceof Map<?,?> map) {
            map.forEach((k, v) -> process(v));       // 处理Map数据
        } else if (object instanceof Object[] arr) {
            for (Object o : arr) { process(o); }                            // 处理数组数据
        } else {
            sensitive(object);                                              // 获取单独对象后进行处理
        }

        return object;
    }

    // 首先获取Object及父类的目标字段，再进行处理
    private void sensitive(Object object) {
        if (object == null) {
            return;
        }

        // 获取由@Sensitive和@DeepSensitive注解标记的字段
        List<Field> targetFileList = getTargetFileList(object);

        // 处理字段脱敏
        handleFieldsSensitive(object, targetFileList);
    }

    // 获取目标字段集合
    private List<Field> getTargetFileList(Object object) {
        List<Field> fieldList = new ArrayList<>();
        // 获取当前及父级所有字段
        getDeepField(object.getClass(), fieldList);
        // 返回目标字段
        return fieldList;
    }

    // 深度获取目标字段，汉父类
    private void getDeepField(Class<?> objectClass, List<Field> fieldList) {
        // 从缓存中获取Field集合，不存在就添加
        List<Field> fields = null;
        try {
            fields = FIELD_CACHE.get(objectClass, () -> Arrays.stream(objectClass.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Sensitive.class) || field.isAnnotationPresent(DeepSensitive.class))
                    .collect(Collectors.toList()));
        } catch (ExecutionException e) {
            throw new SensitiveException("获取Fields失败");
        }
        // 向集合中插入
        fieldList.addAll(fields);
        // 获取父类
        Class<?> superclass = objectClass.getSuperclass();
        if (superclass == null) {
            return;
        }
        // 父类不为Object，再次尝试获取字段
        if (!superclass.equals(Object.class)) {
            getDeepField(superclass, fieldList);
        }
    }

    // 处理字段脱敏
    private void handleFieldsSensitive(Object object, List<Field> fields) {
        for (Field field : fields) {
            field.setAccessible(true);
            // 判断是否标记的对应注解
            if (field.isAnnotationPresent(Sensitive.class)) {
                // 获取注解
                Sensitive annotation = field.getAnnotation(Sensitive.class);
                // 根据角色编码忽略数据脱敏
                String[] ignoreRoleCodes = annotation.ignoreRoleCodes();
                boolean ignore = new HashSet<>(Arrays.stream(ignoreRoleCodes).toList()).containsAll(LoginUserContext.getRoleCodeList());
                if (!ignore) {
                    Object value = null;
                    try {
                        value = field.get(object);
                    } catch (IllegalAccessException e) {
                        throw new SensitiveException("取值异常，属性名称为：" + field.getName());
                    }
                    if (value == null) {
                        return;
                    }
                    // 通过注解获取替换函数
                    Function<String, String> function = annotation.type().getFunction();
                    String fieldName = field.getName();
                    // 根据val的不同类型进行脱敏处理
                    if (value instanceof String val) {
                        sensitiveString(function, field, object, val);
                    } else if (value instanceof Collection<?> collection) {
                        sensitiveCollection(function, fieldName, collection);
                    } else if (value instanceof Map<?, ?> map) {
                        sensitiveMap(function, fieldName, map);
                    } else if (value instanceof Object[] array) {
                        sensitiveArray(function, fieldName, array);
                    } else {
                        log.error("数据脱敏异常，无法处理 {} 类型的字段 {}", field.getType() ,fieldName);
                    }
                }
            } else if (field.isAnnotationPresent(DeepSensitive.class)) {
                // 当字段标记了@DeepSensitive注解，取到该字段再次执行process方法进行处理
                try {
                    process(field.get(object));
                } catch (IllegalAccessException e) {
                    throw new SensitiveException("DeepSensitive获取属性失败，属性名称为：" + field.getName());
                }
            }
        }
    }

    // 字符串脱敏，使用反射重新set
    private void sensitiveString(Function<String, String> function, Field field, Object object, String val) {
        try {
            field.set(object, function.apply(val));
        } catch (IllegalAccessException e) {
            throw new SensitiveException("赋值异常，属性名称为：" + field.getName());
        }
    }

    // 集合类型脱敏，新集合缓存脱敏的值后旧集合清空后通过addAll存入
    private void sensitiveCollection(Function<String, String> function, String fieldName, Collection<?> collection) {
        // 集合类型先将collection处理后保存为list，collection清空后将list元素设置到collection中
        List list = collection.stream().map(val -> {
            if (val == null) {
                return null;
            }
            if (val instanceof String v) {
                return function.apply(v);
            } else {
                throw new SensitiveException("集合元素无法转换为字符串，属性名称为：" + fieldName);
            }
        }).toList();
        collection.clear();
        collection.addAll(list);
    }

    // map类型脱敏，新map缓存脱敏的值后map清空后通过putAll存入
    private void sensitiveMap(Function<String, String> function, String fieldName, Map map) {
        Map hashMap = new HashMap(map.size());
        // map 类型遍历重新赋值
        map.forEach((key,val) -> {
            if (val == null) {
                hashMap.put(key, null);
            } else if (val instanceof String v) {
                hashMap.put(key, function.apply(v));
            } else {
                throw new SensitiveException("Map中Value无法转换为字符串，属性名称为：" + fieldName);
            }
        });
        map.clear();
        map.putAll(hashMap);
    }

    // 数组脱敏，按索引重新赋值
    private void sensitiveArray(Function<String, String> function, String fieldName, Object[] array) {
        // 数组类型遍历后重新赋值
        for (int i = 0; i < array.length; i++) {
            Object objItem = array[i];
            if (objItem == null) {
                array[i] = null;
            } else if (objItem instanceof String s) {
                array[i] = function.apply(s);
            } else {
                throw new SensitiveException("数组元素无法转换为字符串，属性名称为：" + fieldName);
            }
        }
    }
}
