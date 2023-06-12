package com.example.healthgenie.controller;

import com.example.healthgenie.domain.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.exception.PtReviewErrorResult;
import com.example.healthgenie.exception.PtReviewException;
import com.example.healthgenie.global.exception.GlobalExceptionHandler;
import com.example.healthgenie.service.PtReviewService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserPtReviewControllerTest {


    @InjectMocks
    private UserPtReviewController target;

    private MockMvc mockMvc;

    private Gson gson;

    @Mock
    private PtReviewService ptReviewService;

    @BeforeEach
    public void init(){
        gson = new Gson();

        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void 후기작성성공() throws Exception {


        //given
        final String url = "/User/Pt/review";

        //when

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(ptReviewRequestDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                );

        //then

        resultActions.andExpect(status().isOk());
    }
    @Test
    public void 후기작성실패_트레이너_존재하지않음() throws Exception {

        //given
        final String url = "/User/Pt/review";
        final Long userId=1L;
        doThrow(new PtReviewException(PtReviewErrorResult.TRAINER_EMPTY)).when(ptReviewService).addPtReview(ptReviewRequestDto(),userId);


        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(ptReviewRequestDto()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then

        resultActions.andExpect(status().isBadRequest());

    }
    @Test
    public void 후기작성실패_후기가_이미존재함() throws Exception {


        //given
        final String url = "/User/Pt/review";
        final Long userId=1L;
        doThrow(new PtReviewException(PtReviewErrorResult.DUPLICATED_REVIEW)).when(ptReviewService).addPtReview(ptReviewRequestDto(),userId);


        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(ptReviewRequestDto()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then

        resultActions.andExpect(status().isBadRequest());
    }

    public PtReviewRequestDto ptReviewRequestDto(){

        return PtReviewRequestDto.builder()
                .title("jingwon")
                .build();


    }







}
