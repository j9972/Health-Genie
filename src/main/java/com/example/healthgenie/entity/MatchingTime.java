package com.example.healthgenie.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name ="matching_time_tb")
public class MatchingTime {

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
