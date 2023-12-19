//package com.example.healthgenie.boundedContext.chat.dto;
//
//import com.example.healthgenie.base.utils.DateUtils;
//import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class RoomResponse {
//
//    private Long id;
//    private String senderNickname;
//    private String receiverNickname;
//    private boolean isSenderOut;
//    private boolean isReceiverOut;
//    private String createdDate;
//    private String createdTime;
//    private String modifiedDate;
//    private String modifiedTime;
//
//    public static RoomResponse of(ChatRoom room) {
//        return RoomResponse.builder()
//                .id(room.getId())
//                .senderNickname(room.getSender().getNickname())
//                .receiverNickname(room.getReceiver().getNickname())
//                .isSenderOut(room.isSenderOut())
//                .isReceiverOut(room.isReceiverOut())
//                .createdDate(DateUtils.toDate(room.getCreatedDate()))
//                .createdTime(DateUtils.toTime(room.getCreatedDate()))
//                .modifiedDate(DateUtils.toDate(room.getLastModifiedDate()))
//                .modifiedTime(DateUtils.toTime(room.getLastModifiedDate()))
//                .build();
//    }
//}
