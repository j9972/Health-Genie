package com.example.healthgenie.domain.todo.dto;

import com.example.healthgenie.domain.todo.entity.Status;
import com.example.healthgenie.domain.todo.entity.Todo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class TodoResponseDto {
    private Long id;
    /*
        request로 오는 string 날짜 형식을 LocalDateTime에 바인딩
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate todoDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime todoTime;
    private String title;
    private String description;
    private Status status;
    private Long userId;

    public static TodoResponseDto of(Todo todo) {
        return TodoResponseDto.builder()
                .id(todo.getId())
                .todoDate(todo.getTodoDate())
                .todoTime(todo.getTodoTime())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .status(todo.getStatus())
                .userId(todo.getMember().getId())
                .build();
    }
}
