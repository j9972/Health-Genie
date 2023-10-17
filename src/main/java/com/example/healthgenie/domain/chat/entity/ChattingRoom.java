package com.example.healthgenie.domain.chat.entity;

import com.example.healthgenie.domain.ptrecord.entity.PtProcess;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "CHATTING_ROOM_TB")
public class ChattingRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_room_id")
    private Long id;

    // 전송 상태 체크
    @Column(name = "chat_room_status")
    private boolean chatRoomStatus;

    @Column(name = "chat_room_name")
    private String chatRoomName;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "chattingRoom",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();
}
