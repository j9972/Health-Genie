package com.example.healthgenie.base.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    /**
     * date format : 2023.01.01
     * time format : 23:59:59
     * @return 날짜와 시간 정보로 설정된 LocalDateTime 객체 반환
     */
    public static LocalDateTime toLocalDateTime(String date, String time) {
        String dateTimeString = date + " " + time;

        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    /**
     * date format : 2023.01.01
     * @return 날짜 정보만 설정된 LocalDateTime 객체 반환
     */
    public static LocalDateTime toLocalDateTime(String date) {
        String dateTimeString = date + " 00:00:00";

        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public static LocalDateTime toLocalDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.of(23, 59, 59));
    }

    public static LocalDateTime toLocalDateTime(LocalTime time) {
        return LocalDateTime.of(LocalDate.of(9999, 12, 31), time);
    }

    /**
     * date format : 2023.01.01
     * @return LocalDate 객체 반환
     */
    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    /**
     * date format : 23:59:59 || 23:59 (초 생략)
     * @return LocalTime 객체 반환
     */
    public static LocalTime toLocalTime(String time) {
        String[] split = time.split(":");
        if(split.length < 3) {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:00"));
        }
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * @return LocalDateTime 객체에 담긴 날짜 정보 문자열 반환
     */
    public static String toStringDate(LocalDateTime dateTime) {
        String year = String.valueOf(dateTime.getYear());
        String month = String.valueOf(dateTime.getMonthValue());
        if(month.length() == 1) {
            month = addZero(month);
        }
        String day = String.valueOf(dateTime.getDayOfMonth());
        if(day.length() == 1) {
            day = addZero(day);
        }
        return year + "." + month + "." + day;
    }

    public static String toStringDate(LocalDate date) {
        return toStringDate(toLocalDateTime(date));
    }

    /**
     * @return LocalDateTime 객체에 담긴 시간 정보 문자열 반환
     */
    public static String toStringTime(LocalDateTime dateTime) {
        String hour = String.valueOf(dateTime.getHour());
        if(hour.length() == 1) {
            hour = addZero(hour);
        }
        String minute = String.valueOf(dateTime.getMinute());
        if(minute.length() == 1) {
            minute = addZero(minute);
        }
        String second = String.valueOf(dateTime.getSecond());
        if(second.length() == 1) {
            second = addZero(second);
        }
        return hour + ":" + minute + ":" + second;
    }

    public static String toStringTime(LocalTime time) {
        return toStringTime(toLocalDateTime(time));
    }

    private static String addZero(String value) {
        return "0" + value;
    }
}
