package com.lihua.common.utils.date;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 时间日期工具类
 */
public class DateUtils {

    /**
     * 时间日期格式化
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期格式化
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        return System.currentTimeMillis();
    }

    /**
     * 时间戳转 LocalDateTime
     */
    public static LocalDateTime fromTimestamp(long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
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
    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * 格式化日期
     */
    public static String format(LocalDate localDate) {
        return localDate.format(DATE_FORMATTER);
    }

    /**
     * 时间日期字符串转 LocalDateTime
     */
    public static LocalDateTime parseDateTime(String datetime) {
        return LocalDateTime.parse(datetime, DATE_TIME_FORMATTER);
    }

    /**
     * 日期字符串转 LocalDate
     */
    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    /**
     * 两时间相差的分钟数
     */
    public static long differenceMinute(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return Duration.between(startDateTime, endDateTime).toMinutes();
    }
}
