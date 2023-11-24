package com.example.healthgenie.boundedContext.todo.controller;

import com.example.healthgenie.boundedContext.todo.dto.TodoRequestDto;
import com.example.healthgenie.boundedContext.todo.dto.TodoResponseDto;
import com.example.healthgenie.boundedContext.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calender/todo")
@Slf4j
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/write") // http://localhost:1234/calender/todo/write
    public ResponseEntity addTodo(@RequestBody TodoRequestDto dto){

        Long userId = dto.getUserId();
        TodoResponseDto result = todoService.addTodoList(dto, userId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/{userId}") // http://localhost:1234/calender/todo/{userId}
    public List<TodoResponseDto> getTodos(@PathVariable Long userId) {
        return todoService.getAllMyTodo(userId);
    }

    // 수정
    @PatchMapping("/update/{todoId}") // http://localhost:1234/calender/todo/update/{todoId}
    public ResponseEntity updateTodo(@RequestBody TodoRequestDto dto, @PathVariable Long todoId){

        Long id = todoService.update(dto,todoId);
        return new ResponseEntity(id,HttpStatus.OK);
    }

    // 본인만 삭제 가능하게 하기 -> 프론트에서 기능을 숨기면 되어서 구별 로직뺌
    @DeleteMapping("/delete/{todoId}") // http://localhost:1234/calender/todo/delete/{todoId}
    public ResponseEntity deleteTodo(@PathVariable Long todoId) {

        todoService.deletePtReview(todoId);

        return new ResponseEntity("todo가 삭제가 성공했습니다",HttpStatus.OK);
    }
}
