package com.example.healthgenie.boundedContext.chat.dto;

import com.example.healthgenie.boundedContext.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResponse {

    private Long roomId;
    private String nickname;
    private Role role;
    private String profilePhoto;
//    private String lastMessage;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
//    private LocalDateTime createdAt;

//    public static ChatRoomResponse of(ChatRoomUser chatRoomUser) {
//        List<ChatMessage> chatMessages = chatRoomUser.getChatRoom().getChatMessages();
//        String messageContent = chatMessages.isEmpty() ? "마지막 메세지가 없습니다." : chatMessages.get(chatMessages.size() - 1).getMessageContent();
//        return ChatRoomResponse.builder()
//                .roomId(chatRoomUser.getChatRoom().getId()+"")
//                .lastMessage(messageContent)
//                .createdAt(chatRoomUser.getChatRoom().getCreatedDate())
//                .build();
//    }
}
