package com.example.healthgenie.domain.chat.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_maessage_tb")
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @Column(name = "message_content")
    private String messageContent;

    @Column(name = "reading")
    private boolean reading;

    @Column(name = "status")
    private Integer status;

    @Column(name = "chatting_time")
    private Date chattingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="room_id")
    private ChattingRoom chattingRoom;




}
