package com.example.healthgenie.boundedContext.chat.dto;


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

    private LocalDateTime createdDate;
    private String senderEmail;
    private String content;

    public static MessageResponse of(ChatMessage message) {
        return MessageResponse.builder()
                .createdDate(message.getCreatedDate())
                .senderEmail(message.getSender().getEmail())
                .content(message.getMessageContent())
                .build();
    }

    public static List<MessageResponse> of(List<ChatMessage> messages) {
        return messages.stream()
                .map(MessageResponse::of)
                .toList();
    }
}
