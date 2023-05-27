package com.example.healthgenie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "user_pt_review_tb")
public class UserPtReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_pt_review_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "trainer_name")
    private String trainerName;

    @Column(name = "review_content")
    private String reviewContent;


    @Lob
    private Blob pic1;

    @Lob
    private Blob pic2;

    @Lob
    private Blob pic3;

    @Column(name ="star_score")
    private int starScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="trainer_id")
    private User trainer;

    @OneToOne
    @JoinColumn(name = "trainer_pt_application_id")
    private TrainerPtApplication matching;
}
