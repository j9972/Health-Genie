package com.example.healthgenie.boundedContext.matching.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "MATCHING_TB")
public class Matching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="matching_id")
    private Long id;

    @Column(name = "pt_date")
    private LocalDate date;

    @Column(name = "pt_time")
    private LocalTime time;

    @Column(name = "pt_place")
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(name = "pt_state")
    private MatchingState state; // 매칭 상태 필드

    @Column(name = "pt_description")
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "matching", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MatchingUser> matchingUsers = new ArrayList<>();

    public void updateState(MatchingState state) {
        this.state= state;
    }
}
