package com.example.healthgenie.service;

import com.example.healthgenie.dto.CommunitiyPostResponseDto;
import com.example.healthgenie.dto.CommunityPostGetResponseDto;
import com.example.healthgenie.dto.CommunityPostRequestDto;
import com.example.healthgenie.entity.CommunityPost;
import com.example.healthgenie.entity.User;
import com.example.healthgenie.exception.CommunityPostErrorResult;
import com.example.healthgenie.exception.CommunityPostException;
import com.example.healthgenie.exception.PtReviewException;
import com.example.healthgenie.repository.CommunityPostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class CommunityPostServiceTest {

    @InjectMocks
    CommunityPostService target;
    @Mock
    CommunityPostRepository repository;



    @Test
    public void 게시물작성성공(){

        //given
        Long userId = 1L;
        CommunityPostRequestDto savedto = CommunityPostRequestDto.builder().body("hi").build();
        CommunityPost saveResult = buildpost(savedto,userId);
        doReturn(saveResult).when(repository).save(any(CommunityPost.class));

        //when
        final CommunitiyPostResponseDto result = target.addPost(savedto,userId);

        //then
        assertThat(result.getPostId()).isNotNull();

    }

    @Test
    public void 게시물작성실패(){

    }

    @Test
    public void 게시물조회성공(){

        //given
        Long postId =1L;
        doReturn(Optional.of(CommunityPost.builder().id(1L).build())).when(repository).findById(any(Long.class));

        //when
        CommunityPostGetResponseDto result = target.getPost(postId);

        //then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    public void 게시물조회실패_게시물존재하지않음(){

        //given
        Long postId =1L;
        doReturn(Optional.empty()).when(repository).findById(any(Long.class));

        //when
        final CommunityPostException result = assertThrows(CommunityPostException.class, () -> target.getPost(postId));

        //then
        assertThat(result.getCommunityPostErrorResult()).isEqualTo(CommunityPostErrorResult.POST_EMPTY);

    }



    public CommunityPost buildpost(CommunityPostRequestDto dto,Long userId){
        return CommunityPost.builder()
                .id(1L)
                .body(dto.getBody())
                .likeCount(dto.getLikeCount())
                .pics(dto.getPics())
                .title(dto.getTitle())
                .user(User.builder().id(userId).build())
                .build();
    }

}
