package com.example.healthgenie.service;

import com.example.healthgenie.dto.CommunitiyPostResponseDto;
import com.example.healthgenie.dto.CommunityPostGetResponseDto;
import com.example.healthgenie.dto.CommunityPostRequestDto;
import com.example.healthgenie.entity.CommunityPost;
import com.example.healthgenie.entity.User;
import com.example.healthgenie.exception.CommunityPostErrorResult;
import com.example.healthgenie.exception.CommunityPostException;
import com.example.healthgenie.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityPostService {

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
