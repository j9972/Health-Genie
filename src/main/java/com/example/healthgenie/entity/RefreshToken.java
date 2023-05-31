package com.example.healthgenie.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User member;
}
