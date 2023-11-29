package com.example.healthgenie.boundedContext.trainer.entity;

import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.base.entity.BaseEntity;
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

    @Column(name = "name")
    private String name;

    // 아래 4개는 관리페이지에서도 사용
    // 소개글
    @Column(name = "introduction")
    private String introduction;

    // 약력
    @Column(name = "career_history")
    private String career;

    // 경력 [ 개월 수 ]
    @Column(name = "career_month")
    private int careerMonth;

    // 견적
    @Column(name = "cost")
    private int cost;

    @Column(name = "uniname")
    private String uniname;

    // google이나 이런데에서 처음에 어떻게 사진을 가져올 지 몰라서 혹시 몰라 만든 Col
    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "member_cnt")
    private int memberCnt;

    @Column(name = "start_time")
    private int startTime;

    @Column(name = "end_time")
    private int endTime;

    @Column(name = "contact_time")
    private int contactTime;

    @Column(name = "review_avg")
    private Double reviewAvg;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User member;

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updateCost(int cost) {
        this.cost = cost;
    }

    public void updateCareer(String career) {
        this.career = career;
    }

    public void updateMonth(int month) {
        this.careerMonth = month;
    }

}
