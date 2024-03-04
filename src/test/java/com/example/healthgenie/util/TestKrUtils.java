package com.example.healthgenie.util;

import com.example.healthgenie.boundedContext.chat.entity.Room;
import com.example.healthgenie.boundedContext.chat.entity.RoomUser;
import com.example.healthgenie.boundedContext.chat.repository.RoomRepository;
import com.example.healthgenie.boundedContext.community.comment.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.comment.entity.Comment;
import com.example.healthgenie.boundedContext.community.comment.repository.CommentRepository;
import com.example.healthgenie.boundedContext.community.photo.entity.Photo;
import com.example.healthgenie.boundedContext.community.photo.repository.PhotoRepository;
import com.example.healthgenie.boundedContext.community.post.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.repository.PostRepository;
import com.example.healthgenie.boundedContext.matching.dto.MatchingCondition;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.dto.MatchingResponse;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.matching.service.MatchingService;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TestKrUtils {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PhotoRepository photoRepository;
    private final MatchingRepository matchingRepository;
    private final MatchingUserRepository matchingUserRepository;
    private final MatchingService matchingService;

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

    public Post createPost(User writer, String title, String content, List<Photo> photos) {
        Post post = Post.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .photos(photos)
                .build();

        return postRepository.save(post);
    }

    public Post createPost(User writer, String title, String content) {
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
                .content(content)
                .userId(senderId)
                .roomId(roomId)
                .build();
    }

    public Room createChatRoom(User sender, User receiver) {
        RoomUser s = RoomUser.builder()
                .user(sender)
                .active(true)
                .build();

        RoomUser r = RoomUser.builder()
                .user(receiver)
                .active(true)
                .build();

        Room room = Room.builder()
                .roomUsers(List.of(s, r))
                .roomHashCode(sender.getId() > receiver.getId() ? Objects.hash(sender.getId(), receiver.getId()) : Objects.hash(receiver.getId(), sender.getId()))
                .active(true)
                .build();

        return roomRepository.save(room);
    }

    public PostRequest createPostRequest(String title, String content) {
        return PostRequest.builder()
                .title(title)
                .content(content)
                .build();
    }

    public CommentRequest createCommentRequest(String content) {
        return CommentRequest.builder()
                .content(content)
                .build();
    }

    public Comment createComment(String content, Post post) {
        User writer = createUser("기본 사용자", Role.EMPTY, "default@test.com");

        Comment comment = Comment.builder()
                .commentBody(content)
                .writer(writer)
                .post(post)
                .build();

        return commentRepository.save(comment);
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

    public Matching createMatching(Long userId, Long trainerId, String date, String time, String place, String description) {
        MatchingResponse save = matchingService.save(userId, trainerId, date, time, place, description);
        return matchingRepository.findById(save.getId()).get();
    }

    public MatchingCondition createMatchingCondition(String date, String time) {
        return MatchingCondition.builder()
                .date(date)
                .time(time)
                .build();
    }

    public MatchingCondition createMatchingCondition(String date) {
        return MatchingCondition.builder()
                .date(date)
                .build();
    }
}
