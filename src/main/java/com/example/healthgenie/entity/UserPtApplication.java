package com.example.healthgenie.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name ="user_pt_application_tb")
public class UserPtApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pt_application_id")
    private Long id;

    @Column(name = "pt_apply_date")
    private String ptApplyDate;

    @Column(name = "pt_start_date")
    private String ptStartDate;

    @Column(name = "pt_times")
    private String ptTimes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="trainer_id")
    private User trainer;


}
