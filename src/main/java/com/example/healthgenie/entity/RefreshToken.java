package com.example.healthgenie.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "refresh_token")
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
