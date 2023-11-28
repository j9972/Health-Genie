package com.example.healthgenie.boundedContext.chat.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CHAT_MESSAGE_TB")
@Builder
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @NotNull
    @Column(name = "message_content")
    private String messageContent;

    @ManyToOne
    @JoinColumn(name ="sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name ="room_id")
    private ChatRoom chatRoom;

//    @Builder.Default
//    @OneToMany(mappedBy = "chatMessage",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    private List<ChatMessagePhoto> chatMessagePhotoList = new ArrayList<>();

}
