package com.example.healthgenie.boundedContext.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequest {

    private Long userId;
    private Long anotherUserId;
}
