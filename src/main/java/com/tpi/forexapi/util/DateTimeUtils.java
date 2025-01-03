package com.tpi.forexapi.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期時間utils
 */
@Slf4j
public class DateTimeUtils {

    private DateTimeUtils() {}

    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_FORMAT_YYYYMMDD_SLASH = "yyyy/MM/dd";

    /**
     * 把字串轉成日期時間
     *
     * @param dateTime
     * @param format
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String dateTime, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(dateTime, formatter);
        } catch (Exception e) {
            log.error("stringToLocalDateTime error, e={} format={}", e, format);
            throw new RuntimeException(e);
        }
    }

    /**
     * 把字串轉成日期
     *
     * @param dateTime
     * @param format
     * @return
     */
    public static LocalDate stringToLocalDate(String dateTime, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(dateTime, formatter);
        } catch (Exception e) {
            log.error("stringToLocalDate error, e={} format={}", e, format);
            throw new RuntimeException(e);
        }
    }

}
