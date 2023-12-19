//package com.example.healthgenie.boundedContext.chat.service;
//
//import com.example.healthgenie.base.exception.ChatException;
//import com.example.healthgenie.base.exception.CommonException;
//import com.example.healthgenie.base.utils.SecurityUtils;
//import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
//import com.example.healthgenie.boundedContext.chat.dto.RoomResponse;
//import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
//import com.example.healthgenie.boundedContext.chat.repository.ChatRoomQueryRepository;
//import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
//import com.example.healthgenie.boundedContext.user.entity.User;
//import com.example.healthgenie.boundedContext.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//import static com.example.healthgenie.base.exception.ChatErrorResult.*;
//import static com.example.healthgenie.base.exception.CommonErrorResult.USER_NOT_FOUND;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class ChatRoomService {
//
//    private final ChatRoomRepository chatRoomRepository;
//    private final ChatRoomQueryRepository chatRoomQueryRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public RoomResponse joinRoom(RoomRequest request) {
//        User sender = userRepository.findById(request.getSenderId())
//                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));
//
//        User receiver = userRepository.findById(request.getReceiverId())
//                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));
//
//        if(Objects.equals(sender.getId(), receiver.getId())) {
//            throw new ChatException(SELF_CHAT);
//        }
//
//        // 누가 보낸 사람인지 받은 사람인지가 중요한게 아니고, 그 두명이 확실히 포함된 채팅방들을 가져오게 해야함
//        Optional<ChatRoom> opChatRoom = chatRoomQueryRepository.findChatRoomByUsers(sender, receiver);
//        if(opChatRoom.isPresent()) {
//            return RoomResponse.of(opChatRoom.get());
//        }
//
//        ChatRoom chatRoom = ChatRoom.builder()
//                .sender(sender)
//                .receiver(receiver)
//                .build();
//
//        return RoomResponse.of(chatRoomRepository.save(chatRoom));
//    }
//
//    public List<RoomResponse> getRooms() {
//        User currentUser = SecurityUtils.getCurrentUser();
//
//        return chatRoomRepository.findAllBySenderIdOrReceiverId(currentUser.getId(), currentUser.getId()).stream()
//                .map(RoomResponse::of)
//                .toList();
//    }
//
//    public RoomResponse getRoomDetail(Long roomId) {
//        User currentUser = SecurityUtils.getCurrentUser();
//
//        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));
//
//        if(!isRelated(currentUser, chatRoom)) {
//            throw new ChatException(NO_PERMISSION);
//        }
//
//        return RoomResponse.of(chatRoom);
//    }
//
//    @Transactional
//    public String deleteRoom(Long roomId) {
//        User currentUser = SecurityUtils.getCurrentUser();
//
//        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));
//
//        if (!isRelated(currentUser, chatRoom)) {
//            throw new ChatException(NO_PERMISSION);
//        }
//
//        chatRoom.exitRoom(currentUser);
//
//        if(chatRoom.isSenderOut() && chatRoom.isReceiverOut()) {
//            chatRoomRepository.delete(chatRoom);
//        }
//
//        return "채팅방이 삭제 되었습니다.";
//    }
//
//    private boolean isRelated(User user, ChatRoom room) {
//        if(room.getSender() != null) {
//            if(user.getId().equals(room.getSender().getId())) {
//                return true;
//            }
//        }
//        if(room.getReceiver() != null) {
//            if(user.getId().equals(room.getReceiver().getId())) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
