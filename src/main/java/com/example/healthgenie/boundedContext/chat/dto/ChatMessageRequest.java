package com.example.healthgenie.boundedContext.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest implements Serializable {

    private Long messageId;
    private Long roomId; // 공통으로 만들어진 방 번호
    private Long userId;
    private String nickName;
    private Set<Long> otherUserIds; // 상대방
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}