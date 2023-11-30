package com.example.healthgenie.boundedContext.todo.dto;

import com.example.healthgenie.boundedContext.todo.entity.Status;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class TodoResponseDto {
    private Long id;
    /*
        request로 오는 string 날짜 형식을 LocalDateTime에 바인딩
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private String title;
    private String description;
    private Status status;
    private String userMail;
    private boolean pt;

    public static TodoResponseDto of(Todo todo) {
        return TodoResponseDto.builder()
                .id(todo.getId())
                .date(todo.getDate())
                .time(todo.getTime())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .status(todo.getStatus())
                .pt(todo.isPt())
                .userMail(todo.getMember().getEmail())
                .build();
    }
}
