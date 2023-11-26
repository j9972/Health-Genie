package com.example.healthgenie.boundedContext.chat.entity.dto;


import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
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
public class MessageResponse {

    private LocalDateTime sendAt;
    private String sender;
    private String content;

    public static MessageResponse of(ChatMessage message) {
        return MessageResponse.builder()
                .sendAt(message.getCreatedDate())
                .sender(message.getSender().getName())
                .content(message.getMessageContent())
                .build();
    }

    public static List<MessageResponse> of(List<ChatMessage> messages) {
        return messages.stream()
                .map(MessageResponse::of)
                .toList();
    }
}
