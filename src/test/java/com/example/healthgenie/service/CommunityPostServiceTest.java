package com.example.healthgenie.service;

import com.example.healthgenie.dto.*;
import com.example.healthgenie.entity.CommunityPost;
import com.example.healthgenie.entity.User;
import com.example.healthgenie.exception.CommunityPostErrorResult;
import com.example.healthgenie.exception.CommunityPostException;
import com.example.healthgenie.repository.CommunityPostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


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

    @Test
    public void 게시물리스트조회실패_게시물존재하지않음(){


        //
    }


    @Test
    public void 게시물리스트조회성공(){
        //given
        List<CommunityPost> postList = new ArrayList<>();
        for(int i=1;i<50;i++){
            CommunityPost savePost = CommunityPost.builder().title("인덱스는"+i).build();
            postList.add(savePost);
        }
        repository.saveAll(postList);

        Page<CommunityPost> page = Page.empty();
        Pageable pageable = PageRequest.of(1,20);
        doReturn(pageable).when(repository).findAll(any(PageRequest.class));
        //when
        CommunityPostListResponseDto dto = target.getPostList(0);
        //then
        assertThat(dto).isNotNull();

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
