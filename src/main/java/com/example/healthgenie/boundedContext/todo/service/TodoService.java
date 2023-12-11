package com.example.healthgenie.boundedContext.todo.service;

import com.example.healthgenie.base.exception.PtReviewErrorResult;
import com.example.healthgenie.base.exception.PtReviewException;
import com.example.healthgenie.base.exception.TodoErrorResult;
import com.example.healthgenie.base.exception.TodoException;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.repository.MatchingQueryRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.routine.repository.RoutineQueryRepository;
import com.example.healthgenie.boundedContext.todo.dto.TodoRequestDto;
import com.example.healthgenie.boundedContext.todo.dto.TodoResponseDto;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.example.healthgenie.boundedContext.todo.repository.TodoQueryRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.todo.repository.TodoRepository;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;
    private final MatchingRepository matchingRepository;
    private final TodoQueryRepository todoQueryRepository;
    private final MatchingQueryRepository matchingQueryRepository;

    @Transactional
    public TodoResponseDto addTodoList(TodoRequestDto dto){

        User user = SecurityUtils.getCurrentUser();

        Todo todo = Todo.builder()
                .date(dto.getDate())
                .time(dto.getTime())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .member(user)
                .build();
        Todo saved = todoRepository.save(todo);

        return TodoResponseDto.of(saved);
    }

    @Transactional
    public TodoResponseDto update(TodoRequestDto dto, Long todoId){

        Todo todo = authorizationWriter(todoId);

        if(dto.getDate() != null) {
            todo.updateDate(dto.getDate());
        }
        if(dto.getTime() != null) {
            todo.updateTime(dto.getTime());
        }
        if(dto.getTitle() != null) {
            todo.updateTitle(dto.getTitle());
        }
        if(dto.getDescription() != null) {
            todo.updateDescription(dto.getDescription());
        }

        return TodoResponseDto.of(todo);
    }

    public String deleteTodo(Long todoId) {
        // user가 해당 글을 작성한 유저인지 체크 [ DB에 많은 데이터가 쌓이기 때문에 필요 ]
        Todo todo = authorizationWriter(todoId);
        todoRepository.deleteById(todo.getId());

        return "오늘 할일이 삭제되었습니다.";
    }

    public Todo authorizationWriter(Long id) {
        User member = SecurityUtils.getCurrentUser();

        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoException(TodoErrorResult.NO_TODO_INFO));
        if (!todo.getMember().getId().equals(member.getId())) {
            throw new TodoException(TodoErrorResult.WRONG_USER);
        }
        return todo;
    }

    /*
        todo를 전부 띄우는게 아니라 날짜 별로 띄워야 하는게 핵심
     */
    public List<TodoResponseDto> getAllMyTodo(LocalDate date) {

        User currentUser = SecurityUtils.getCurrentUser();

        List<Todo> todos = todoQueryRepository.findAllByMemberIdAndDate(currentUser.getId(), date);

        // 관리페이지에 보내줄 데이터 [ 매칭 날짜랑 오늘이 같으면 데이터 보내주기 ]
        LocalDateTime dateTime = date.atStartOfDay();

        List<Matching> matching = matchingQueryRepository.findAllOneDayByDateAndId(dateTime, currentUser.getId());

        // 매칭이 있으면
        if (!matching.isEmpty()) {
            // pt boolean값 업데이트 해주기
            for(Todo todo: todos) {
                if(!todo.isPt()) {
                    todo.updatePt(true);
                }
            }
        }

        // 오늘 날짜와 cli에서 보내준 날짜가 다르다면 Todo list 반환
        return todos.stream()
                .map(TodoResponseDto::of)
                .collect(toList());

    }
}
