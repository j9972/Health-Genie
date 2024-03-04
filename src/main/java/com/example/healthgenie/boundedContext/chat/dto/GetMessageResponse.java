package com.example.healthgenie.boundedContext.chat.dto;

import com.example.healthgenie.boundedContext.chat.entity.Message;
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

    public GetMessageResponse(Message message) {
        this.userId = message.getSender().getId();
        this.nickname = message.getSender().getNickname();
        this.message = message.getContent();
        this.createdAt = message.getCreatedDate();
    }

    public GetMessageResponse(ChatMessageRequest request) {
        this.message = request.getContent();
        this.createdAt = LocalDateTime.now(); // 현재시간 저장
    }

    public static GetMessageResponse of(Message message) {
        return GetMessageResponse.builder()
                .userId(message.getSender().getId())
                .nickname(message.getSender().getNickname())
                .message(message.getContent())
                .createdAt(message.getCreatedDate())
                .build();
    }
}