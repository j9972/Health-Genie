package com.example.healthgenie.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "schdule_time_tb")
public class SchduleTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schdule_time_id")
    private Long id;

    @Column(name = "day")
    private String day;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;
}
