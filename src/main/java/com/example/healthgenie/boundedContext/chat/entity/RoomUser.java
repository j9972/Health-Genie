package com.example.healthgenie.boundedContext.chat.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ROOM_USER_TB")
public class RoomUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    private String name;

    private boolean active;

    public void inactive() {
        this.active = false;
    }

    public void active() {
        this.active = true;
    }
}
