package com.example.healthgenie.domain.trainer.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "trainer_pt_possible_time_tb")
public class TrainerPtPossibleTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_pt_possible_time_id")
    private Long id;

    @Column(name = "day")
    private String day;

    @Column(name ="start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private User trainer;
}
