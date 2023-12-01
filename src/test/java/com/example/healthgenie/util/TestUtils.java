package com.example.healthgenie.util;

import com.example.healthgenie.boundedContext.chat.dto.MessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.entity.CommunityPostPhoto;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestUtils {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final CommunityPostRepository communityPostRepository;

    public void login(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
    }

    public User createUser(String name, Role role, String email) {
        User user = User.builder()
                .name(name)
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
                .build();

        return communityPostRepository.save(post);
    }

    public CommunityPost createPost(User writer, String title, String content) {
        return createPost(writer, title, content, null);
    }

    public RoomRequest createRoomRequest(String receiverEmail) {
        return RoomRequest.builder()
                .receiverEmail(receiverEmail)
                .build();
    }

    public MessageRequest createMessageRequest(Long roomId, String content, String senderEmail) {
        return MessageRequest.builder()
                .content(content)
                .roomId(roomId)
                .senderEmail(senderEmail)
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
}
