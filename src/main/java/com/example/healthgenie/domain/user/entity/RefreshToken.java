package com.example.healthgenie.domain.user.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "REFRESHTOKEN_TB")
public class RefreshToken extends BaseEntity {


    @Id
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User member;

    public void changeToken(String token) {
        this.token = token;
    }

}
