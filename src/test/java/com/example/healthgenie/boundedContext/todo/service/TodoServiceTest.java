package com.example.healthgenie.boundedContext.todo.service;

import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.exception.TodoException;
import com.example.healthgenie.boundedContext.todo.dto.TodoRequestDto;
import com.example.healthgenie.boundedContext.todo.dto.TodoResponseDto;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TodoServiceTest {

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    TodoService todoService;

    User user;
    Todo todoTest;

    @BeforeEach
    void before() {
        user = testKrUtils.createUser("test1", Role.USER,"jh485200@gmail.com");

        LocalDate localDate = LocalDate.of(2023, 12, 15);
        LocalTime localTime = LocalTime.of(14, 30, 45); // 시, 분, 초

        todoTest = testSyUtils.createTodo(localDate, localTime, "test title", "test description", user);
    }

    @Test
    @DisplayName("정상적으로 todo list 작성")
    void addTodoList() {
        // given
        testKrUtils.login(user);

        LocalDate date = LocalDate.of(2023, 12, 15);
        LocalTime time = LocalTime.of(14, 30, 45); // 시, 분, 초


        TodoRequestDto dto = testSyUtils.TodoRequestDto(
                date, time, "test title", "description test");

        // when
        TodoResponseDto todo = todoService.addTodoList(dto);

        // then
        assertThat(todo.getDate()).isEqualTo(date);
        assertThat(todo.getTime()).isEqualTo(time);
        assertThat(todo.getTitle()).isEqualTo("test title");
        assertThat(todo.getDescription()).isEqualTo("description test");
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 todo list 작성 실패하기")
    void notLoginAddTodoList() {
        // given

        TodoRequestDto dto = testSyUtils.TodoRequestDto(
                LocalDate.now(), LocalTime.now(), "test title", "description test");

        // when


        // then
        assertThatThrownBy(() -> todoService.addTodoList(dto))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("정상적으로 todo list 수정하기")
    void update() {
        // given
        testKrUtils.login(user);

        // when
        TodoRequestDto dto = testSyUtils.TodoRequestDto("수정한 제목", "수정한 내용");

        // then
        TodoResponseDto response = todoService.update(dto, todoTest.getId());

        assertThat(response.getTitle()).isEqualTo("수정한 제목");
        assertThat(response.getDescription()).isEqualTo("수정한 내용");
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 todo list 수정 실패하기")
    void notLoginUpdate() {
        // given

        // when
        TodoRequestDto dto = testSyUtils.TodoRequestDto("수정한 제목", "수정한 내용");

        // then
        assertThatThrownBy(() -> todoService.update(dto, todoTest.getId()))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("정상적인 todo 삭제하기")
    void deleteTodo() {
        // given
        testKrUtils.login(user);

        // when

        // then
        String response = todoService.deleteTodo(todoTest.getId());

        assertThat(response).isEqualTo("오늘 할일이 삭제되었습니다.");
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 todo 삭제하기")
    void notLoginDeleteTodo() {
        // given

        // when

        // then
        assertThatThrownBy(() -> todoService.deleteTodo(todoTest.getId()))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("존재하지 않는 todo 삭제하기")
    void notExistTodoDelete() {
        // given
        testKrUtils.login(user);

        // when

        // then
        assertThatThrownBy(() -> todoService.deleteTodo(2000L))
                .isInstanceOf(TodoException.class);
    }

    @Test
    @DisplayName("정상적인 todo list 전체 조회하기")
    void getAllMyTodo() {
        // given
        testKrUtils.login(user);

        LocalDate date = LocalDate.of(2023, 12, 15);
        LocalTime time = LocalTime.of(14, 30, 45); // 시, 분, 초

        for(int i=1; i<=5; i++) {
            TodoRequestDto dto = testSyUtils.TodoRequestDto(date, time, "test title", "description test");
            todoService.addTodoList(dto);
        }

        // when
        List<TodoResponseDto> response = todoService.getAllMyTodo(date);

        // then
        assertThat(response.size()).isEqualTo(6);


    }
}