package com.example.healthgenie.boundedContext.chat.dto;

import com.example.healthgenie.base.utils.DateUtils;
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
    private Long senderId;
    private Long receiverId;
    private boolean isSenderOut;
    private boolean isReceiverOut;
    private String createdDate;
    private String createdTime;
    private String modifiedDate;
    private String modifiedTime;

    public static RoomResponse of(ChatRoom room) {
        return RoomResponse.builder()
                .id(room.getId())
                .senderId(room.getSender().getId())
                .receiverId(room.getReceiver().getId())
                .isSenderOut(room.isSenderOut())
                .isReceiverOut(room.isReceiverOut())
                .createdDate(DateUtils.toDate(room.getCreatedDate()))
                .createdTime(DateUtils.toTime(room.getCreatedDate()))
                .modifiedDate(DateUtils.toDate(room.getLastModifiedDate()))
                .modifiedTime(DateUtils.toTime(room.getLastModifiedDate()))
                .build();
    }
}
