package com.example.healthgenie.util;

import com.example.healthgenie.boundedContext.chat.dto.ChatMessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomRequest;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoomUser;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.community.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.entity.CommunityComment;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.entity.CommunityPostPhoto;
import com.example.healthgenie.boundedContext.community.repository.CommunityCommentRepository;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostPhotoRepository;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostRepository;
import com.example.healthgenie.boundedContext.matching.dto.MatchingCondition;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
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
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TestKrUtils {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final CommunityPostRepository communityPostRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final CommunityPostPhotoRepository communityPostPhotoRepository;
    private final MatchingRepository matchingRepository;


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
                .writer(writer)
                .title(title)
                .content(content)
                .communityPostPhotos(photos)
                .build();

        return communityPostRepository.save(post);
    }

    public CommunityPost createPost(User writer, String title, String content) {
        return createPost(writer, title, content, new ArrayList<>());
    }


    public ChatRoomRequest createRoomRequest(Long senderId, Long receiverId) {
        return ChatRoomRequest.builder()
                .userId(senderId)
                .anotherUserId(receiverId)
                .build();
    }

    public ChatMessageRequest createMessageRequest(Long senderId, Long roomId) {
        return ChatMessageRequest.builder()
                .userId(senderId)
                .roomId(roomId)
                .build();
    }

    public ChatMessageRequest createMessageRequest(String content, Long senderId, Long roomId) {
        return ChatMessageRequest.builder()
                .message(content)
                .userId(senderId)
                .roomId(roomId)
                .build();
    }

    public ChatRoom createChatRoom(User sender, User receiver) {
        ChatRoomUser s = ChatRoomUser.builder()
                .user(sender)
                .active(true)
                .build();

        ChatRoomUser r = ChatRoomUser.builder()
                .user(receiver)
                .active(true)
                .build();

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomUsers(List.of(s, r))
                .roomHashCode(sender.getId() > receiver.getId() ? Objects.hash(sender.getId(), receiver.getId()) : Objects.hash(receiver.getId(), sender.getId()))
                .active(true)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    public PostRequest createPostRequest(String title, String content, Long writerId) {
        return createPostRequest(title, content, writerId, null);
    }

    public PostRequest createPostRequest(String title, String content, Long writerId, List<MultipartFile> photos) {
        return PostRequest.builder()
                .title(title)
                .content(content)
                .writerId(writerId)
                .photos(photos)
                .build();
    }

    public CommentRequest createCommentRequest(String content, Long writerId) {
        return CommentRequest.builder()
                .content(content)
                .writerId(writerId)
                .build();
    }

    public CommunityComment createComment(String content, CommunityPost post) {
        User writer = createUser("기본 사용자", Role.EMPTY, "default@test.com");

        CommunityComment comment = CommunityComment.builder()
                .commentBody(content)
                .writer(writer)
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

    public MatchingRequest createMatchingRequest(String date, String time, String place, String description, Long userId, Long trainerId) {
        return MatchingRequest.builder()
                .date(date)
                .time(time)
                .place(place)
                .description(description)
                .userId(userId)
                .trainerId(trainerId)
                .build();
    }

    public MatchingRequest createMatchingRequest(String date, String time, Long userId, Long trainerId) {
        return createMatchingRequest(date, time, "", "", userId, trainerId);
    }

    public MatchingRequest createMatchingRequest(String date, Long userId, Long trainerId) {
        return createMatchingRequest(date, "00:00:00", "", "", userId, trainerId);
    }

    public Matching createMatching(LocalDateTime date, String place, String description, User member, User trainer) {
        Matching matching = Matching.builder()
                .date(date)
                .place(place)
                .description(description)
                .member(member)
                .trainer(trainer)
                .state(MatchingState.DEFAULT)
                .build();

        return matchingRepository.save(matching);
    }

    public MatchingCondition createMatchingCondition(String date, String time, Long userId, Long trainerId) {
        return MatchingCondition.builder()
                .date(date)
                .time(time)
                .userId(userId)
                .trainerId(trainerId)
                .build();
    }

    public MatchingCondition createMatchingCondition(String date, Long userId, Long trainerId) {
        return MatchingCondition.builder()
                .date(date)
                .userId(userId)
                .trainerId(trainerId)
                .build();
    }
}
