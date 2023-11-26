package com.example.healthgenie.boundedContext.routine.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "GENIE_ROUTINE_TB")
public class GenieRoutine extends BaseEntity {

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
    @ElementCollection
    @Column(name = "workout_part")
    private List<String> parts = new ArrayList<>();

    // 지니 추천에서 간략한 설명을 위함
    @Column(name = "content")
    private String content;

    /*
        이름, sets * reps 순서로 데이터 넣기
     */
    @NotNull
    @ElementCollection
    @Column(name = "workout")
    private List<String> workout = new ArrayList<>();

}
