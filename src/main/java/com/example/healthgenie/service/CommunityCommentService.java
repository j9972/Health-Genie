package com.example.healthgenie.service;

import com.example.healthgenie.domain.community.dto.*;
import com.example.healthgenie.domain.community.entity.CommunityComment;
import com.example.healthgenie.domain.community.entity.CommunityPost;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.CommunityCommentErrorResult;
import com.example.healthgenie.exception.CommunityCommentException;
import com.example.healthgenie.repository.CommunityCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityCommentService {

    private final CommunityCommentRepository commentRepository;
    public CommunityCommentResponseDto addComment(CommunityCommentRequestDto dto, Long postId, Long userId) {
        postId = 1L;
        userId = 1L;

        log.info("CommunityCommentService");

        CommunityComment saveResult = commentRepository.save(buildComment(dto, postId, userId));

        // getId()를 건네주는 이유는 CommunityCommentResponseDto 에 post_id뿐 이기 때문이다
        return new CommunityCommentResponseDto(saveResult.getId());
    }

    public CommunityComment buildComment(CommunityCommentRequestDto dto, Long postId, Long userId){
        return CommunityComment.builder()
                .commentBody(dto.getCommentBody())
                .likeCount(dto.getLikeCount())
                .post(CommunityPost.builder().id(postId).build())
                .member(User.builder().id(userId).build())
                .build();
    }

    // 1개의 comment 조회
    public CommunityCommentGetResponseDto getComment(Long commentId) {
        Optional<CommunityComment> optionalResult = commentRepository.findById(commentId);

        // 찾은 댓글이 없을 수 있는 경우의 수들을 throw 처리
        if(optionalResult.isEmpty() || !optionalResult.isPresent() || optionalResult==null){
            throw new CommunityCommentException(CommunityCommentErrorResult.COMMENT_EMPTY);
        }

        CommunityComment comment = optionalResult.get();
        CommunityCommentGetResponseDto result = CommunityCommentGetResponseDto.builder()
                .id(comment.getId())
                .commentBody(comment.getCommentBody())
                .likeCount(comment.getLikeCount())
                .build();
        return result;
    }
}
