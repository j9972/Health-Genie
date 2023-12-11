package com.example.healthgenie.boundedContext.todo.dto;

import com.example.healthgenie.boundedContext.todo.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequestDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    private String title;
    private String description;
    private Status status;
    private String userNickname;
    private boolean pt;
}
