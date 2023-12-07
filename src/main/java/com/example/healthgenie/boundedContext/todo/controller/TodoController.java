package com.example.healthgenie.boundedContext.todo.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.todo.dto.TodoRequestDto;
import com.example.healthgenie.boundedContext.todo.dto.TodoResponseDto;
import com.example.healthgenie.boundedContext.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calender/todo")
@Slf4j
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/write") // http://localhost:1234/calender/todo/write
    public ResponseEntity<Result> addTodo(@RequestBody TodoRequestDto dto){

        TodoResponseDto response = todoService.addTodoList(dto);
        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{date}") // http://localhost:1234/calender/todo/{date}
    public ResponseEntity<Result> getTodos(@PathVariable LocalDate date) {
        List<TodoResponseDto> response = todoService.getAllMyTodo(date);
        return ResponseEntity.ok(Result.of(response));
    }

    // 수정
    @PatchMapping("/update/{todoId}") // http://localhost:1234/calender/todo/update/{todoId}
    public ResponseEntity<Result> updateTodo(@RequestBody TodoRequestDto dto, @PathVariable Long todoId){

        TodoResponseDto response = todoService.update(dto,todoId);
        return ResponseEntity.ok(Result.of(response));
    }

    // 본인만 삭제 가능하게 하기 -> 프론트에서 기능을 숨기면 되어서 구별 로직뺌
    @DeleteMapping("/delete/{todoId}") // http://localhost:1234/calender/todo/delete/{todoId}
    public ResponseEntity<Result> deleteTodo(@PathVariable Long todoId) {

        String response = todoService.deleteTodo(todoId);

        return ResponseEntity.ok(Result.of(response));
    }
}
