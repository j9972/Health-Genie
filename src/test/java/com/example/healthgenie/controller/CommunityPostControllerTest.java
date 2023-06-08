package com.example.healthgenie.controller;

import com.example.healthgenie.dto.CommunityPostRequestDto;
import com.example.healthgenie.exception.CommunityPostErrorResult;
import com.example.healthgenie.exception.CommunityPostException;
import com.example.healthgenie.exception.PtReviewErrorResult;
import com.example.healthgenie.exception.PtReviewException;
import com.example.healthgenie.global.exception.GlobalExceptionHandler;
import com.example.healthgenie.service.CommunityPostService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommunityPostControllerTest {

    @InjectMocks
    private CommunityPostController target;

    private MockMvc mockMvc;

    private Gson gson;

    @Mock
    private CommunityPostService service;


    @BeforeEach
    public void init(){

        gson = new Gson();

        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void 게시물작성실패(){

    }


    @Test
    public void 게시물작성성공() throws Exception {


        //given
        CommunityPostRequestDto dto = CommunityPostRequestDto.builder().build();
        String url = "/community/post/add";

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(gson.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());
    }
    @Test
    public void 게시물조회성공() throws Exception {

        //given
        String url = "/community/post/get";
        Long postId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .param("postId",String.valueOf(postId)));
        //then
        resultActions.andExpect(status().isOk());

    }


    @Test
    public void 게시물조회실패_게시물없음() throws Exception {
        //given
        String url = "/community/post/get";
        Long postId = 1L;
        doThrow(new CommunityPostException(CommunityPostErrorResult.POST_EMPTY)).when(service).getPost(postId);

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("postId",String.valueOf(postId)));
        //then

        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    public void 게시물리스트조회성공() throws Exception {

        //given
        String url = "/communtiy/post";
        int page = 1;
        //when

        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .param("page",String.valueOf(page),"size",String.valueOf(20))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());



    }


}
