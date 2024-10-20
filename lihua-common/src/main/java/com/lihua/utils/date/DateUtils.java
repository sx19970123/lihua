package com.lihua.utils.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 时间日期工具类
 */
public class DateUtils {

    /**
     * 获取当前时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     */
    public static LocalDate nowDate() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间戳
     */
    public static long nowTimeStamp() {
        return timeStamp(now());
    }

    /**
     * 获取指定时间的时间戳
     */
    public static long timeStamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 格式化时间
     */
    public static String format(LocalDateTime localDateTime,String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 格式化日期
     */
    public static String format(LocalDate localDate,String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return localDate.format(dateTimeFormatter);
    }

    /**
     * 两时间相差的分钟数
     */
    public static long differenceMinute(LocalDateTime time1, LocalDateTime time2) {
        long difference = timeStamp(time1) - timeStamp(time2);
        return difference / (60 * 1000);
    }
}
