package com.example.healthgenie.service;

import com.example.healthgenie.domain.community.dto.CommunityPostResponseDto;
import com.example.healthgenie.domain.community.dto.CommunityPostGetResponseDto;
import com.example.healthgenie.domain.community.dto.CommunityPostRequestDto;
import com.example.healthgenie.domain.community.entity.CommunityPost;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.CommunityPostErrorResult;
import com.example.healthgenie.exception.CommunityPostException;
import com.example.healthgenie.repository.CommunityPostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        final CommunityPostResponseDto result = target.addPost(savedto,userId);

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
    void 게시물리스트조회실패_페이지번호오류() {
        // Set up the mock behavior for the repository
        int page = -1;
        int size = 20;
        final CommunityPostException result = assertThrows(CommunityPostException.class, () -> target.getPostsByPage(page,size));

        // Perform the test
        assertThat(result.getCommunityPostErrorResult()).isEqualTo(CommunityPostErrorResult.PAGE_EMPTY);

    }
    @Test
    public void 게시물리스트조회성공(){
        // Given
        List<CommunityPost> dummyData = createDummyPosts(40);  // 40 dummy data 생성
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(0, 20,sort); // 첫 번째 페이지, 페이지당 20개 아이템, sort는 날짜 최신순
        Page<CommunityPost> dummyPage = new PageImpl<>(dummyData, pageable, 60); //더미페이지 생성
        when(repository.findAll(pageable)).thenReturn(dummyPage);  // findAll 메서드가 dummyPage 반환하도록 설정
        // When
        Page<CommunityPost> result = target.getPostsByPage(0,20);
        // Then
        assertEquals(3, result.getTotalPages());  // 전체 페이지 수는 2여야 함
        assertEquals(60, result.getTotalElements());  // 전체 게시물 수는 40개여야 함
        assertThat(result.getSize()).isEqualTo(20); //현재 페이지 게시물은 20개 여야함

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
    private List<CommunityPost> createDummyPosts(int count) {
        List<CommunityPost> dummyPosts = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            CommunityPost post = CommunityPost.builder()
                    .id(Long.valueOf(i))
                    .title("title"+i)
                    .build();
            dummyPosts.add(post);
        }
        return dummyPosts;
    }

}
