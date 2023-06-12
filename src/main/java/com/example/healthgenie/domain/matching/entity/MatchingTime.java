package com.example.healthgenie.domain.matching.entity;

import com.example.healthgenie.domain.trainer.entity.TrainerPtApplication;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name ="matching_time_tb")
public class MatchingTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="matching_time_id")
    private Long id;

    @Column(name ="day")
    private String day;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_pt_application_id")
    private TrainerPtApplication trainerPtApplication;
}
