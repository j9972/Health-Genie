package com.example.healthgenie.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "chatting_room")
public class ChattingRoom extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_room_id")
    private Long id;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User memeber;

    @OneToOne
    @JoinColumn(name = "trainer_id")
    private User trainer;
}
