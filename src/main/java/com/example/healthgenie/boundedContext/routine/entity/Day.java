package com.example.healthgenie.boundedContext.routine.entity;

public enum Day {
    MONDAY("월요일"),
    TUESDAY("화요일"),
    WEDNESDAY("수요일"),
    THURSDAY("목요일"),
    FRIDAY("금요일"),
    SATURDAY("토요일"),
    SUNDAY("일요일");

    private String day;

    private Day(String day) {
        this.day = day;
    }

    // Getter
    // Day d = Day.MONDAY;
    // d.getDay(); 이런식으로 사용하기
    public String getDay() {
        return day;
    }
}
