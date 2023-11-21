package com.example.healthgenie.domain.todo.dto;

import com.example.healthgenie.domain.todo.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
public class TodoRequestDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate todoDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime todoTime;

    private String title;
    private String description;
    private Status status;
    private Long userId;
}
