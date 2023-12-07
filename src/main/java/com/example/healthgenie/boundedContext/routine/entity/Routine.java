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

import static com.example.healthgenie.base.exception.CommonErrorResult.ALREADY_EXISTS_ROLE;

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

    // 피그마보면 운동 부위를 한번에 가져온다.
    @ElementCollection
    private List<String> part = new ArrayList<>();

    @Embedded // 내장타입이다.
    private WorkoutRecipe workoutRecipe;

    // 여기서 이름이 genie는 해당 서비스 추천 루틴
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User member;

    public void updateDay(Day day) {
        this.day = day;
    }


    public void updateContent(String content) {
        this.content = content;
    }

    public void updatePart(List<String> part) {
        this.part = part;
    }
}

