package com.example.healthgenie.boundedContext.todo.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.todo.dto.TodoRequestDto;
import com.example.healthgenie.boundedContext.todo.dto.TodoResponseDto;
import com.example.healthgenie.boundedContext.todo.dto.TodoUpdateRequest;
import com.example.healthgenie.boundedContext.todo.service.TodoService;
import com.example.healthgenie.boundedContext.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calender/todo")
@Slf4j
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<Result> addTodo(@RequestBody TodoRequestDto dto,
                                          @AuthenticationPrincipal User user) {

        TodoResponseDto response = todoService.addTodoList(dto, user);
        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{date}")
    public ResponseEntity<Result> getTodos(@PathVariable LocalDate date,
                                           @AuthenticationPrincipal User user) {

        List<TodoResponseDto> response = todoService.getAllMyTodo(date, user);
        return ResponseEntity.ok(Result.of(response));
    }

    // 수정
    @PatchMapping("/{todoId}")
    public ResponseEntity<Result> updateTodo(@RequestBody TodoUpdateRequest dto,
                                             @PathVariable Long todoId,
                                             @AuthenticationPrincipal User user) {

        TodoResponseDto response = todoService.update(dto, todoId, user);
        return ResponseEntity.ok(Result.of(response));
    }

    // 본인만 삭제 가능하게 하기 -> 프론트에서 기능을 숨기면 되어서 구별 로직뺌
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Result> deleteTodo(@PathVariable Long todoId,
                                             @AuthenticationPrincipal User user) {

        String response = todoService.deleteTodo(todoId, user);

        return ResponseEntity.ok(Result.of(response));
    }
}
