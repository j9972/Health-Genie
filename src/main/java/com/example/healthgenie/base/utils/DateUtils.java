package com.example.healthgenie.base.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    /**
     * date format : 2023-01-01
     * time format : 23:59:59
     * @return 날짜와 시간 정보로 설정된 LocalDateTime 객체 반환
     */
    public static LocalDateTime toLocalDateTime(String date, String time) {
        String dateTimeString = date + " " + time;

        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * date format : 2023-01-01
     * @return 날짜 정보만 설정된 LocalDateTime 객체 반환
     */
    public static LocalDateTime toLocalDateTime(String date) {
        String dateTimeString = date + " 00:00:00";

        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * @return LocalDateTime 객체에 담긴 날짜 정보 문자열 반환
     */
    public static String toDate(LocalDateTime dateTime) {
        String year = String.valueOf(dateTime.getYear());
        String month = String.valueOf(dateTime.getMonthValue());
        if(month.length() == 1) {
            month = addZero(month);
        }
        String day = String.valueOf(dateTime.getDayOfMonth());
        if(day.length() == 1) {
            day = addZero(day);
        }
        return year + "-" + month + "-" + day;
    }

    /**
     * @return LocalDateTime 객체에 담긴 시간 정보 문자열 반환
     */
    public static String toTime(LocalDateTime dateTime) {
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

    private static String addZero(String value) {
        return "0" + value;
    }
}
