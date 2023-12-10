package com.example.healthgenie.boundedContext.chat.dto;


import com.example.healthgenie.base.utils.DateUtils;
import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {

    private String date;
    private String time;
    private Long senderId;
    private String content;

    public static MessageResponse of(ChatMessage message) {
        return MessageResponse.builder()
                .date(DateUtils.toDate(message.getCreatedDate()))
                .time(DateUtils.toTime(message.getCreatedDate()))
                .senderId(message.getSender().getId())
                .content(message.getMessageContent())
                .build();
    }

    public static List<MessageResponse> of(List<ChatMessage> messages) {
        return messages.stream()
                .map(MessageResponse::of)
                .toList();
    }
}
