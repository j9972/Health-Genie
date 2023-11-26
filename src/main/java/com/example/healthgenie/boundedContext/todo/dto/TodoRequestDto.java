package com.example.healthgenie.boundedContext.todo.dto;

import com.example.healthgenie.boundedContext.todo.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TodoRequestDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate todoDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime todoTime;

    private String title;
    private String description;
    private Status status;
    private String userMail;
}
