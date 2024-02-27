package com.example.healthgenie.boundedContext.community.service;

import com.example.healthgenie.boundedContext.community.entity.Post;
import com.example.healthgenie.boundedContext.community.entity.Photo;
import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.boundedContext.community.repository.PhotoRepository;
import com.example.healthgenie.boundedContext.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.healthgenie.base.exception.CommunityPostErrorResult.PHOTO_EMPTY;
import static com.example.healthgenie.base.exception.CommunityPostErrorResult.POST_EMPTY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final PostRepository postRepository;

    @Transactional
    public Photo save(Long postId, String path) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        Photo photo = Photo.builder()
                .postPhotoPath(path)
                .post(post)
                .build();

        return photoRepository.save(photo);
    }

    @Transactional
    public List<Photo> saveAll(Long postId, List<String> photoPaths) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        List<Photo> photos = photoPaths.stream()
                .map(path -> Photo.builder()
                        .postPhotoPath(path)
                        .post(post)
                        .build())
                .toList();

        // 객체 그래프 탐색용
        for (Photo photo : photos) {
            post.addPhoto(photo);
        }

        return photoRepository.saveAll(photos);
    }

    public Photo findById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(PHOTO_EMPTY));
    }

    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    @Transactional
    public List<Photo> updateAll(Long postId, List<String> photoPaths) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        // 객체 그래프 탐색용
        post.removePhotos(post.getPhotos());

        // 기존 Photo 삭제
        photoRepository.deleteAllByPostId(postId);

        // 새로운 Photo 저장
        return saveAll(postId, photoPaths);
    }
}
