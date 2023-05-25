package com.example.healthgenie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name ="trainer_pt_application")
public class TrainerPtApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_pt_application_id")
    private Long id;

    @Column(name = "pt_apply_date")
    private String ptApplyDate;

    @Column(name = "pt_start_date")
    private String ptStartDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "Matching_state", length = 20)
    private MatchingState matchingState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="trainer_id")
    private User trainer;


}
