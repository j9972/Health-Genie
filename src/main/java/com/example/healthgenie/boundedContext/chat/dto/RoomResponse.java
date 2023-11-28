package com.example.healthgenie.boundedContext.chat.dto;

import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {

    private Long id;
    private String senderEmail;
    private String receiverEmail;

    public static RoomResponse of(ChatRoom room) {
        return RoomResponse.builder()
                .id(room.getId())
                .senderEmail(room.getSender().getEmail())
                .receiverEmail(room.getReceiver().getEmail())
                .build();
    }
}
