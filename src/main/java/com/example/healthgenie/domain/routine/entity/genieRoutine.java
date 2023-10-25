package com.example.healthgenie.domain.routine.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "WORKOUT_ROUTINE_GENIE_TB")
public class genieRoutine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="genie_routine_id")
    private Long id;

    @NotNull
    @Column(name = "workout_part")
    private String workoutPart;

    @NotNull
    @Column(name = "workout_name")
    private String name;

    @NotNull
    @Column(name = "workout_day")
    private String workoutDay;

    @NotNull
    @Column(name = "level")
    private String level;

    @Column(name = "workout_sets")
    private int workoutSets;

    @Column(name = "workout_reps")
    private int workoutReps;
}

