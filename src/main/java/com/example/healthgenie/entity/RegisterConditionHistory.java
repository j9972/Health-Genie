package com.example.healthgenie.entity;


import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "register_condition_history_tb")
public class RegisterConditionHistory extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_condition_history_id")
    private Long id;

    @Column(name = "register_condition")
    private boolean registerCondition;

    @Column(name = "privacy_info")
    private boolean privacyInfo;

    @Column(name = "email_reception")
    private boolean emailReception;

    @Column(name = "email")
    private String email;

    @Column(name = "agree_date")
    private String agreeDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User member;

}
