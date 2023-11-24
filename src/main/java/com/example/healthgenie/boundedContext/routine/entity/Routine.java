package com.example.healthgenie.boundedContext.routine.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/*
    공통
 */

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "ROUTINE_TB")
public class Routine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="routine_id")
    private Long id;

    // genie 추천 운동에서 하 / 중 / 상급자를 나누는 level
    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @NotNull
    @Column(name = "workout_day")
    @Enumerated(EnumType.STRING)
    private Day day;

    @NotNull
    @ElementCollection // 별도의 테이블을 만들어서 컬렉션 관리해줌
    @Column(name = "workout_part")
    private List<String> parts = new ArrayList<>();

    // 지니 추천에서 간략한 설명을 위함
    @Column(name = "content")
    private String content;

    // name이라는 단어 사용시 작성자 이름일것이라는 오해 사전 방지를 위한 workoutName 이라는 이름을 갖음
    @NotNull
    @Column(name = "workout_name")
    private String workoutName;

    @NotNull
    @Column(name = "workout_sets")
    private int sets;

    @NotNull
    @Column(name = "workout_reps")
    private int reps;

    // 여기서 이름이 genie는 해당 서비스 추천 루틴
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User member;

    public void updateDay(Day day) {
        this.day = day;
    }

    public void updateParts(List<String> parts) {
        this.parts = parts;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public void updateSets(int sets) {
        this.sets = sets;
    }

    public void updateReps(int reps) {
        this.reps = reps;
    }

}

