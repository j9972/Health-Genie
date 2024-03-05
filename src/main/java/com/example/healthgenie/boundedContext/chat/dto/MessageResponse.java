package com.example.healthgenie.boundedContext.chat.dto;


import com.example.healthgenie.boundedContext.chat.entity.Message;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
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
    private Long senderId;
    private String nickname;
    private Role role;
    private String profilePhoto;
    private String content;

    public static MessageResponse of(Message message) {
        return MessageResponse.builder()
                .createdDate(message.getCreatedDate())
                .senderId(message.getSender().getId())
                .nickname(message.getSender().getNickname())
                .role(message.getSender().getRole())
                .profilePhoto(message.getSender().getProfilePhoto())
                .content(message.getContent())
                .build();
    }

    public static List<MessageResponse> of(List<Message> messages) {
        return messages.stream()
                .map(MessageResponse::of)
                .toList();
    }
}
