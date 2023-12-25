package com.example.healthgenie.boundedContext.chat.dto;

import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMessageResponse {
    private Long userId;
    private String nickname;
    private String message; // type이 image일 경우 객체 URL이 담김
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public GetMessageResponse(ChatMessage message) {
        this.userId = message.getSender().getId();
        this.nickname = message.getSender().getNickname();
        this.message = message.getMessageContent();
        this.createdAt = message.getCreatedDate();
    }

    public GetMessageResponse(ChatMessageRequest request) {
        this.userId = request.getUserId();
        this.nickname = request.getNickName();
        this.message = request.getMessage();
        this.createdAt = LocalDateTime.now(); // 현재시간 저장
    }

    public static GetMessageResponse of(ChatMessage message) {
        return GetMessageResponse.builder()
                .userId(message.getSender().getId())
                .nickname(message.getSender().getNickname())
                .message(message.getMessageContent())
                .createdAt(message.getCreatedDate())
                .build();
    }
}