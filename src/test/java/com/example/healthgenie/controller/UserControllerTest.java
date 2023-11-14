package com.example.healthgenie.controller;

import com.example.healthgenie.domain.user.dto.UserRegisterDto;
import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.exception.GlobalExceptionHandler;
import com.example.healthgenie.repository.UserRepository;
import com.example.healthgenie.service.UserServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController target;

    private MockMvc mockMvc;

    private Gson gson;

    @Mock
    UserServiceImpl service;

    @Mock
    UserRepository repository;

    @BeforeEach
    public void init(){
        gson = new Gson();

        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

    }

    @Test
    void MockMVCIsNotNull() throws Exception {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }
    /*

    @Test
    void signUp실패_이메일형식아님() throws Exception {
        //given
        String url = "/api/v1/auth/signup";

        UserRegisterDto dto = UserRegisterDto.builder()
                .email("notEmail")
                .password("1234")
                .name("test")
                .uniName("knu")
                .role(Role.ADMIN)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                .content(gson.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void signUp실패_이메일isNull() throws Exception {
        //given
        String url = "/api/v1/auth/signup";

        UserRegisterDto dto = UserRegisterDto.builder()
                .email(null)
                .password("1234")
                .name("test")
                .uniName("knu")
                .role(Role.ADMIN)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void signUp실패_nameIsNull() throws Exception {
        //given
        String url = "/api/v1/auth/signup";

        UserRegisterDto dto = UserRegisterDto.builder()
                .email("test@naver.com")
                .password("1234")
                .name(null)
                .uniName("knu")
                .role(Role.ADMIN)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void signUp실패_PwdIsNull() throws Exception {
        // given
        String url = "/api/v1/auth/signup";

        UserRegisterDto dto = UserRegisterDto.builder()
                .email("test@naver.com")
                .password(null)
                .name("test")
                .uniName("knu")
                .role(Role.ADMIN)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void signUp() throws Exception {

        //given
        String url = "/api/v1/auth/signup";

        UserRegisterDto dto = UserRegisterDto.builder()
                .email("test@naver.com")
                .password("1234")
                .name("test")
                .uniName("knu")
                .role(Role.ADMIN)
                .build();
        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(gson.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());

    }

    @Test
    void authMail() {
    }

    @Test
    void validMailCode() {
    }

    @Test
    void login() throws Exception {
        //given
        String url = "/api/v1/auth/login";

        User user = User.builder()
                .email("test@naver.com")
                .password("1234")
                .uniName("knu")
                .name("test")
                .role(Role.ADMIN)
                .provider("testCase")
                .build();

        repository.save(user);

        String email = "test@naver.com";
        String password = "1234";

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }
 */
}