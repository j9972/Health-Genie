package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.dto.RoomResponse;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.healthgenie.base.exception.ChatErrorResult.*;
import static com.example.healthgenie.base.exception.CommonErrorResult.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long joinChatRoom(RoomRequest request) {
        User sender = SecurityUtils.getCurrentUser();

        User receiver = userRepository.findByEmail(request.getReceiverEmail())
                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        if(Objects.equals(sender.getId(), receiver.getId())) {
            throw new ChatException(SELF_CHAT);
        }

        Optional<ChatRoom> opChatRoom = chatRoomRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
        if(opChatRoom.isPresent()) {
            return opChatRoom.get().getId();
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        return chatRoomRepository.save(chatRoom).getId();
    }

    public List<RoomResponse> getRoomList() {
        User currentUser = SecurityUtils.getCurrentUser();

        return chatRoomRepository.findAllBySenderIdOrReceiverId(currentUser.getId(), currentUser.getId()).stream()
                .map(RoomResponse::of)
                .toList();
    }

    public RoomResponse getRoomDetail(Long roomId) {
        User currentUser = SecurityUtils.getCurrentUser();

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        if(!isRelated(currentUser, chatRoom)) {
            throw new ChatException(NO_PERMISSION);
        }

        return RoomResponse.of(chatRoom);
    }

    @Transactional
    public String deleteRoom(Long roomId) {
        User currentUser = SecurityUtils.getCurrentUser();

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        if (!isRelated(currentUser, chatRoom)) {
            throw new ChatException(NO_PERMISSION);
        }

        chatRoom.exitRoom(currentUser);

        if(chatRoom.getSender() == null && chatRoom.getReceiver() == null) {
            chatRoomRepository.delete(chatRoom);
        }

        return "채팅방이 삭제 되었습니다.";
    }

    private boolean isRelated(User user, ChatRoom room) {
        if(room.getSender() != null) {
            if(user.getId().equals(room.getSender().getId())) {
                return true;
            }
        }
        if(room.getReceiver() != null) {
            if(user.getId().equals(room.getReceiver().getId())) {
                return true;
            }
        }
        return false;
//        return Objects.equals(user.getId(), room.getSender().getId()) || Objects.equals(user.getId(), room.getReceiver().getId());
    }
}
