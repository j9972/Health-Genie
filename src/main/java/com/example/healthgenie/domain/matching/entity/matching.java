package com.example.healthgenie.domain.matching.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "MATCHING_TB")
public class matching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_pt_review_id")
    private Long id;


    @Column(name = "pt_place")
    private String ptPlace;

    @Column(name = "pt_matching_date")
    private LocalDateTime date;

    @Column(name = "pt_accept")
    private boolean ptAccept;

    @Column(name = "pt_price")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="trainer_id")
    private User trainer;
}
