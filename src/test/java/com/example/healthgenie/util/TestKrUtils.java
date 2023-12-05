package com.example.healthgenie.util;

import com.example.healthgenie.boundedContext.chat.dto.MessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.community.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.entity.CommunityComment;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.entity.CommunityPostPhoto;
import com.example.healthgenie.boundedContext.community.repository.CommunityCommentRepository;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostPhotoRepository;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostRepository;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestKrUtils {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final CommunityPostRepository communityPostRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final CommunityPostPhotoRepository communityPostPhotoRepository;

    public void login(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
    }

    public User createUser(String name, Role role, String email) {
        User user = User.builder()
                .name(name)
                .nickname(name)
                .role(role)
                .email(email)
                .build();

        return userRepository.save(user);
    }

    public CommunityPost createPost(User writer, String title, String content, List<CommunityPostPhoto> photos) {
        CommunityPost post = CommunityPost.builder()
                .member(writer)
                .title(title)
                .content(content)
                .communityPostPhotos(photos)
                .communityPostPhotos(new ArrayList<>())
                .build();

        return communityPostRepository.save(post);
    }

    public CommunityPost createPost(User writer, String title, String content) {
        return createPost(writer, title, content, null);
    }

    public RoomRequest createRoomRequest(String receiverNickname) {
        return RoomRequest.builder()
                .receiverNickname(receiverNickname)
                .build();
    }

    public MessageRequest createMessageRequest(Long roomId, String content, String senderNickname) {
        return MessageRequest.builder()
                .content(content)
                .roomId(roomId)
                .senderNickname(senderNickname)
                .build();
    }

    public ChatRoom createChatRoom(User sender, User receiver) {
        ChatRoom chatRoom = ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    public PostRequest createPostRequest(String title, String content) {
        return createPostRequest(title, content, null);
    }

    public PostRequest createPostRequest(String title, String content, List<MultipartFile> photos) {
        return PostRequest.builder()
                .title(title)
                .content(content)
                .photos(photos)
                .build();
    }

    public CommentRequest createCommentRequest(String content, String writer) {
        return CommentRequest.builder()
                .content(content)
                .writer(writer)
                .build();
    }

    public CommentRequest createCommentRequest(String content) {
        return createCommentRequest(content, null);
    }

    public CommunityComment createComment(String content, CommunityPost post) {
        User writer = createUser("기본 사용자", Role.EMPTY, "default@test.com");

        CommunityComment comment = CommunityComment.builder()
                .commentBody(content)
                .member(writer)
                .post(post)
                .build();

        return communityCommentRepository.save(comment);
    }

    public CommunityPostPhoto createPostPhoto(CommunityPost post, String path) {
        CommunityPostPhoto postPhoto = CommunityPostPhoto.builder()
                .post(post)
                .postPhotoPath(path)
                .build();

        return communityPostPhotoRepository.save(postPhoto);
    }

    public MatchingRequest createMatchingRequest(LocalDateTime date, String place, String description, User member, User trainer) {
        return MatchingRequest.builder()
                .date(date)
                .place(place)
                .description(description)
                .userNickname(member.getNickname())
                .trainerNickname(trainer.getNickname())
                .build();
    }
}
