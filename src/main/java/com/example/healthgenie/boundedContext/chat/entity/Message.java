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
@Table(name = "MESSAGE_TB")
@Builder
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @NotNull
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name ="sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name ="room_id")
    private Room room;
}
