package com.example.healthgenie.domain.ptreview.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "PT_REVIEW_TB")
public class PtReivew extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_pt_review_id")
    private Long id;

    @NotNull
    @Column(name = "review_content")
    private String content;

    // 자동으로 시간을 저장 안하는 이유는 후기 작성 날짜를 내가 설정할 수 있게 해야하기 때문
    @Column(name = "pt_review_date")
    private Date ptReviewDate;

    @NotNull
    @Column(name = "stop_reason")
    private String stopReason;

    @Column(name = "review_score")
    private Double reviewScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="trainer_id")
    private User trainer;
}