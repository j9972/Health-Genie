package com.example.healthgenie.boundedContext.chat.dto;

import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoomUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResponse {

    private String roomId;
    private String lastMessage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public static ChatRoomResponse of(ChatRoomUser chatRoomUser) {
        List<ChatMessage> chatMessages = chatRoomUser.getChatRoom().getChatMessages();
        String messageContent = chatMessages.isEmpty() ? "마지막 메세지가 없습니다." : chatMessages.get(chatMessages.size() - 1).getMessageContent();
        return ChatRoomResponse.builder()
                .roomId(chatRoomUser.getChatRoom().getId()+"")
                .lastMessage(messageContent)
                .createdAt(chatRoomUser.getChatRoom().getCreatedDate())
                .build();
    }
}
