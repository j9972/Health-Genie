package com.example.healthgenie.boundedContext.chat.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "CHAT_MESSAGE_PHOTO_TB")
public class ChatMessagePhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_photo_id")
    private Long id;

    // 여러개 사진이 가능하다
    @Column(name = "chat_message_photo")
    private String photo;

    // FK -> chatMessagePhoto[N] : chatMessage[1]
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="message_id")
    private ChatMessage chatMessage;
}
