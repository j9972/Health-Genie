package com.example.healthgenie.boundedContext.matching.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "MATCHING_TB")
public class Matching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_pt_matching_id")
    private Long id;

    @Column(name = "pt_date")
    private LocalDateTime date;

    @Column(name = "pt_place")
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(name = "pt_participate")
    private MatchingState participateState; // 회원이 PT 최종 참석/취소 결정 필드

    @Enumerated(EnumType.STRING)
    @Column(name = "pt_accept")
    private MatchingState acceptState; // 트레이너가 PT 최종 참석/취소에 대한 승인 필드

    @Column(name = "pt_description")
    private String description;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User member;

    @ManyToOne
    @JoinColumn(name ="trainer_id")
    private User trainer;

    public void updateParticipateState(MatchingState state) {
        this.participateState = state;
    }

    public void updateAcceptState(MatchingState state) {
        this.acceptState = state;
    }
}
