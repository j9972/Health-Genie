package com.example.healthgenie.boundedContext.chat.entity.dto;

import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {

    private Long id;
    private String userName;
    private String trainerName;
    private List<String> messages;

    public static RoomResponse of(ChatRoom room) {
        List<String> messages = room.getChatMessages().stream()
                .map(ChatMessage::getMessageContent)
                .toList();

        return RoomResponse.builder()
                .id(room.getId())
                .userName(room.getUser().getName())
                .trainerName(room.getTrainer().getName())
                .messages(messages)
                .build();
    }
}
