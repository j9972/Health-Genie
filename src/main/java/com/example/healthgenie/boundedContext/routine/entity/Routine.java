package com.example.healthgenie.boundedContext.routine.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @Column(name = "workout_day")
    @Enumerated(EnumType.STRING)
    private Day day;

    // 지니 추천에서 간략한 설명을 위함
    @Column(name = "content")
    private String content;

    @Column(name = "parts")
    private String parts;

    @Embedded // 내장타입이다.
    private WorkoutRecipe workoutRecipe;

    // 여기서 이름이 genie는 해당 서비스 추천 루틴
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User member;

    public void updateDay(Day day) {
        this.day = day;
    }

    public void updatePart(String part) {
        this.parts = part;
    }
}
