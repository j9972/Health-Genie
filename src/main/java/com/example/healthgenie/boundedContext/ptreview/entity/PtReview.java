package com.example.healthgenie.boundedContext.ptreview.entity;

import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.base.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@Table(name = "PT_REVIEW_TB")
public class PtReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_pt_review_id")
    private Long id;

    @NotNull
    @Column(name = "review_content")
    private String content;

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

    public void updateContent(String content) {
        this.content = content;
    }
    public void updateReason(String reason) {
        this.stopReason = reason;
    }
    public void updateScore(double score) {
        this.reviewScore = score;
    }
}