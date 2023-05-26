package com.example.healthgenie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_maessage_tb")
public class ChatMessage {

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
