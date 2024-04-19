package com.example.healthgenie.boundedContext.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.todo.dto.TodoRequestDto;
import com.example.healthgenie.boundedContext.todo.dto.TodoResponseDto;
import com.example.healthgenie.boundedContext.todo.dto.TodoUpdateRequest;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        user = testKrUtils.createUser("jh485200@gmail.com", "test1", AuthProvider.EMPTY, Role.USER);

        LocalDate localDate = LocalDate.of(2023, 12, 15);
        LocalTime localTime = LocalTime.of(14, 30, 45); // 시, 분, 초

        todoTest = testSyUtils.createTodo(localDate, localTime, "test title", "test description", user);
    }

    @Test
    @DisplayName("정상적으로 todo list 작성")
    void add_todo_list() {
        // given
        testKrUtils.login(user);

        LocalDate date = LocalDate.of(2023, 12, 15);
        LocalTime time = LocalTime.of(14, 30, 45); // 시, 분, 초

        TodoRequestDto dto = testSyUtils.TodoRequestDto(
                date, time, "test title", "description test");

        // when
        TodoResponseDto todo = todoService.addTodoList(dto, user);

        // then
        assertThat(todo.getDate()).isEqualTo(date);
        assertThat(todo.getTime()).isEqualTo(time);
        assertThat(todo.getTitle()).isEqualTo("test title");
        assertThat(todo.getDescription()).isEqualTo("description test");
    }

//    @Test
//    @DisplayName("로그인 하지 않은 유저가 todo list 작성 실패하기")
//    void fail_add_todo_list_cuz_of_login() {
//        // given
//        testSyUtils.logout();
//
//        TodoRequestDto dto = testSyUtils.TodoRequestDto(
//                LocalDate.now(), LocalTime.now(), "test title", "description test");
//
//        // when
//
//        // then
//        assertThatThrownBy(() -> {
//            // 해당 메소드 호출
//            todoService.addTodoList(dto, user);
//        }).isInstanceOf(CustomException.class);
//    }

    @Test
    @DisplayName("정상적으로 todo list 수정하기")
    void update() {
        // given
        testKrUtils.login(user);

        // when
        TodoUpdateRequest dto = testSyUtils.updateTodoRequest("수정한 제목", "수정한 내용");

        // then
        TodoResponseDto response = todoService.update(dto, todoTest.getId(), user);

        assertThat(response.getTitle()).isEqualTo("수정한 제목");
        assertThat(response.getDescription()).isEqualTo("수정한 내용");
    }

//    @Test
//    @DisplayName("로그인 하지 않은 유저가 todo list 수정 실패하기")
//    void fail_update_cuz_of_login() {
//        // given
//        testSyUtils.logout();
//
//        // when
//        TodoUpdateRequest dto = testSyUtils.updateTodoRequest("수정한 제목", "수정한 내용");
//
//        // then
//        assertThatThrownBy(() -> {
//            // 해당 메소드 호출
//            todoService.update(dto, todoTest.getId(), user);
//        }).isInstanceOf(CustomException.class);
//    }

    @Test
    @DisplayName("정상적인 todo 삭제하기")
    void delete_todo() {
        // given
        testKrUtils.login(user);

        // when

        // then
        todoService.deleteTodo(todoTest.getId(), user);

        assertThatThrownBy(() -> todoService.deleteTodo(todoTest.getId(), user))
                .isInstanceOf(CustomException.class);
    }

//    @Test
//    @DisplayName("로그인 하지 않은 유저가 todo 삭제하기")
//    void fail_delete_todo_cuz_of_login() {
//        // given
//        testSyUtils.logout();
//
//        // when
//
//        // then
//        assertThatThrownBy(() -> {
//            // 해당 메소드 호출
//            todoService.deleteTodo(todoTest.getId(), user);
//        }).isInstanceOf(CustomException.class);
//    }

    @Test
    @DisplayName("존재하지 않는 todo 삭제하기")
    void fail_todo_delete_cuz_of_no_todo_history() {
        // given
        testKrUtils.login(user);

        // when

        // then
        assertThatThrownBy(() -> todoService.deleteTodo(2000L, user))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("정상적인 todo list 전체 조회하기")
    void get_all_my_todo() {
        // given
        testKrUtils.login(user);

        LocalDate date = LocalDate.of(2023, 12, 15);
        LocalTime time = LocalTime.of(14, 30, 45); // 시, 분, 초

        for (int i = 1; i <= 5; i++) {
            TodoRequestDto dto = testSyUtils.TodoRequestDto(date, time, "test title", "description test");
            todoService.addTodoList(dto, user);
        }

        // when
        List<TodoResponseDto> response = todoService.getAllMyTodo(date, user);

        // then
        assertThat(response.size()).isEqualTo(6);


    }
}