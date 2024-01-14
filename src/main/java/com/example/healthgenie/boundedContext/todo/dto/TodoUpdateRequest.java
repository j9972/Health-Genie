package com.example.healthgenie.boundedContext.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class TodoUpdateRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    private String title;
    private String description;

    public boolean hasDate(){
        return date != null;
    }
    public boolean hasTime(){
        return time != null;
    }
    public boolean hasTitle(){
        return title != null;
    }
    public boolean hasDescription(){
        return description != null;
    }
}