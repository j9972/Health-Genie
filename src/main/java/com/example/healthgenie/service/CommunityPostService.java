package com.example.healthgenie.service;

import com.example.healthgenie.dto.*;
import com.example.healthgenie.entity.CommunityPost;
import com.example.healthgenie.entity.User;
import com.example.healthgenie.exception.CommunityPostErrorResult;
import com.example.healthgenie.exception.CommunityPostException;
import com.example.healthgenie.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityPostService {

    public static final int POST_COUNT = 20;
    private final CommunityPostRepository postRepository;

    //게시물작성
    public CommunitiyPostResponseDto addPost(CommunityPostRequestDto dto, Long userId){
        userId =1L;

        CommunityPost saveResult = postRepository.save(buildPost(dto,userId));
        return new CommunitiyPostResponseDto(saveResult.getId());
    }

    //게시물1개 조회
    public CommunityPostGetResponseDto getPost(Long postId){
        Optional<CommunityPost> optionalResult = postRepository.findById(postId);
        if(optionalResult.isEmpty() || !optionalResult.isPresent() || optionalResult==null){
            throw new CommunityPostException(CommunityPostErrorResult.POST_EMPTY);
        }

        CommunityPost post = optionalResult.get();
        CommunityPostGetResponseDto result = CommunityPostGetResponseDto.builder()
                .id(post.getId())
                .body(post.getBody())
                .title(post.getTitle())
                .likeCount(post.getLikeCount())
                .pics(post.getPics())
                .commentList(post.getCommentList())
                .build();
        return result;
    }

    //게시물 리스트 조회

    public CommunityPostListResponseDto getPostList(int page){


        Page<CommunityPost> list = postRepository.findAll(PageRequest.of(page,POST_COUNT, Sort.by(Sort.Direction.DESC,"createdDate")));

        CommunityPostListResponseDto dto = new CommunityPostListResponseDto();
        List<CommunityPostIdTitleDto> itemList = new ArrayList<>();
        dto.setPageable(list.getPageable());
        dto.setTotalCnt(list.getTotalElements());
        dto.setTotalPages(list.getTotalPages());

        for(CommunityPost i : list){
            itemList.add(new CommunityPostIdTitleDto(i.getId(),i.getTitle()));
        }
        dto.setPostList(itemList);
        return dto;
    }



    public CommunityPost buildPost(CommunityPostRequestDto dto,Long userId){
        return CommunityPost.builder()
                .body(dto.getBody())
                .likeCount(dto.getLikeCount())
                .pics(dto.getPics())
                .title(dto.getTitle())
                .user(User.builder().id(userId).build())
                .build();
    }

}
