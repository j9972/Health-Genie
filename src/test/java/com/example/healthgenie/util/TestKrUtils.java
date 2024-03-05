package com.example.healthgenie.util;

import com.example.healthgenie.boundedContext.chat.dto.MessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.entity.Message;
import com.example.healthgenie.boundedContext.chat.entity.Room;
import com.example.healthgenie.boundedContext.chat.service.MessageService;
import com.example.healthgenie.boundedContext.chat.service.RoomService;
import com.example.healthgenie.boundedContext.community.comment.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.comment.entity.Comment;
import com.example.healthgenie.boundedContext.community.comment.service.CommentService;
import com.example.healthgenie.boundedContext.community.post.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.service.PostService;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.service.MatchingService;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TestKrUtils {

    private final UserService userService;
    private final RoomService roomService;
    private final MessageService messageService;
    private final PostService postService;
    private final CommentService commentService;
    private final MatchingService matchingService;

    public void login(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
    }

    public User createUser(String email, String name, AuthProvider authProvider, Role role) {
        return userService.signUp(email, name, authProvider, role);
    }

    public Post createPost(Long userId, String title, String content) {
        PostRequest request = PostRequest.builder().title(title).content(content).build();
        return postService.save(userId, request);
    }

    public Post createPost(Long userId, String title) {
        return createPost(userId, title, "내용이 없습니다.");
    }

    public Room createRoom(User user, Long anotherUserId) {
        RoomRequest request = RoomRequest.builder().anotherUserId(anotherUserId).build();
        return roomService.saveOrActive(user, request);
    }

    public Comment createComment(Long postId, Long userId, String content) {
        CommentRequest request = CommentRequest.builder().content(content).build();
        return commentService.save(postId, userId, request);
    }

    public Message createMessage(Long roomId, Long senderId, String content) {
        MessageRequest request = MessageRequest.builder().senderId(senderId).content(content).build();
        return messageService.save(roomId, request);
    }

    public Matching createMatching(User trainer, Long userId, LocalDateTime date, String place, String description) {
        MatchingRequest request = MatchingRequest.builder().userId(userId).date(date).place(place).description(description).build();
        return matchingService.save(trainer, request);
    }
}
