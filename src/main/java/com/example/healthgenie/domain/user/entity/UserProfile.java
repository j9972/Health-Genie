package com.example.healthgenie.domain.user.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "USER_PROFILE_TB")
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_profile_id", unique=true)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "height")
    private int height;

    @Column(name = "birth")
    private String birth;

    @Column(name = "weight")
    private int weight;

    @Column(name = "muscle_weight")
    private int muscleWeight;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "gender")
    private String gender;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User member;
}
