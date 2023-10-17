package com.example.healthgenie.domain.trainer.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "TRAINER_INFO_TB")
public class TrainerInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_info_id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "uniname")
    private String uniname;

    // google이나 이런데에서 처음에 어떻게 사진을 가져올 지 몰라서 혹시 몰라 만든 Col
    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "career_history")
    private String career;

    @Column(name = "member_cnt")
    private int memberCnt;

    @Column(name = "career_month")
    private int careerMonth;

    @Column(name = "start_time")
    private int startTime;

    @Column(name = "end_time")
    private int endTime;

    @Column(name = "cost")
    private int cost;

    @Column(name = "contact_time")
    private int contactTime;

    @Column(name = "review_avg")
    private float reviewAvg;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User member;

}
