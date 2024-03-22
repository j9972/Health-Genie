//package com.example.healthgenie.boundedContext.community.post.service;
//
//import com.example.healthgenie.base.exception.Post.CommunityPostException;
//import com.example.healthgenie.base.utils.DateUtils;
//import com.example.healthgenie.base.utils.S3UploadUtils;
//import com.example.healthgenie.base.utils.SecurityUtils;
//import com.example.healthgenie.boundedContext.community.photo.entity.Photo;
//import com.example.healthgenie.boundedContext.community.post.repository.PostRepository;
//import com.example.healthgenie.boundedContext.community.post.dto.PostRequest;
//import com.example.healthgenie.boundedContext.community.post.dto.PostResponse;
//import com.example.healthgenie.boundedContext.community.post.entity.Post;
//import com.example.healthgenie.boundedContext.community.photo.service.PhotoService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import static com.example.healthgenie.base.exception.Post.CommunityPostErrorResult.NO_PERMISSION;
//import static com.example.healthgenie.base.exception.Post.CommunityPostErrorResult.POST_EMPTY;
//
///**
// * 작업 단위를 늘려서 같은 트랜잭션에서 변경이 일어나게 하기 위한 서비스
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class PostTransactionService {
//
//    private final S3UploadUtils s3UploadUtils;
//    private final PostService postService;
//    private final PostRepository postRepository;
//    private final PhotoService photoService;
//
//    public PostResponse save(Long userId, PostRequest request) throws IOException {
//        List<String> photoPaths = new ArrayList<>();
//        Post savedPost = null;
//        try {
//            // 이미지 S3 저장
//            if (existsFile(request)) {
//                photoPaths = s3UploadUtils.upload(request.getPhotos(), "post-photos");
//            }
//
//            // CommunityPostPhoto 엔티티 저장
//            if (existsFile(request)) {
//                photoService.saveAll(savedPost.getId(), photoPaths);
//            }
//
//            // CommunityPost 엔티티 저장
//            savedPost = postService.save(userId, request);
//        } catch (Exception e) {
//            for(String fileUrl : photoPaths) {
//                s3UploadUtils.deleteS3Object("post-photos", fileUrl);
//            }
//            throw e;
//        }
//
//        return PostResponse.builder()
//                .id(savedPost.getId())
//                .createdDate(savedPost.getCreatedDate())
//                .lastModifiedDate(savedPost.getLastModifiedDate())
//                .title(savedPost.getTitle())
//                .content(savedPost.getContent())
//                .writer(savedPost.getWriter().getNickname())
//                .photoPaths(photoPaths)
//                .build();
//    }
//
//    public PostResponse update(Long postId, Long userId, PostRequest request) throws IOException {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));
//
//        if(!Objects.equals(post.getWriter().getId(), SecurityUtils.getCurrentUserId())) {
//            throw new CommunityPostException(NO_PERMISSION);
//        }
//
//        List<String> photoPaths = new ArrayList<>();
//        PostResponse updatedPost = null;
//        try {
//            // 이미지 S3 저장
//            if (existsFile(request)) {
//                List<String> removePaths = post.getPhotos().stream()
//                        .map(Photo::getPath)
//                        .toList();
//
//                for(String fileUrl : removePaths) {
//                    s3UploadUtils.deleteS3Object("post-photos", fileUrl);
//                }
//
//                photoPaths = s3UploadUtils.upload(request.getPhotos(), "post-photos");
//            }
//
//            // CommunityPost 엔티티 저장
//            updatedPost = postService.update(postId, userId, request);
//
//            // CommunityPostPhoto 엔티티 저장
//            if (existsFile(request)) {
//                photoService.updateAll(updatedPost.getId(), photoPaths);
//                updatedPost.setPhotoPaths(photoPaths);
//            } else {
//                updatedPost.setPhotoPaths(new ArrayList<>());
//            }
//        } catch (Exception e) {
//            for(String fileUrl : photoPaths) {
//                s3UploadUtils.deleteS3Object("post-photos", fileUrl);
//            }
//            throw e;
//        }
//
//        LocalDateTime dateTime = post.getCreatedDate();
//        String date = DateUtils.toStringDate(dateTime);
//        String time = DateUtils.toStringTime(dateTime);
//
//        return PostResponse.builder()
//                .id(post.getId())
//                .date(date)
//                .time(time)
//                .title(updatedPost.getTitle())
//                .content(updatedPost.getContent())
//                .writer(updatedPost.getWriter())
//                .photoPaths(updatedPost.getPhotoPaths())
//                .build();
//    }
//
//    public String delete(Long postId, Long userId) throws IOException {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));
//
//        if(!Objects.equals(userId, post.getWriter().getId())) {
//            throw new CommunityPostException(NO_PERMISSION);
//        }
//
//        postService.delete(postId);
//
//        List<String> paths = post.getPhotos().stream()
//                .map(Photo::getPath)
//                .toList();
//
//        for (String path : paths) {
//            s3UploadUtils.deleteS3Object("post-photos", path);
//        }
//
//        return "게시글이 삭제 되었습니다.";
//    }
//
//    private boolean existsFile(PostRequest request) {
//        long totalFileSize = request.getPhotos().stream()
//                .mapToLong(MultipartFile::getSize)
//                .sum();
//
//        return !request.getPhotos().isEmpty() && totalFileSize > 0;
//    }
//}
