package com.example.healthgenie.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name ="application_time_tb")
public class ApplicationTime extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="application_time_id")
    private Long id;

    @Column(name = "day")
    private String day;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

}
