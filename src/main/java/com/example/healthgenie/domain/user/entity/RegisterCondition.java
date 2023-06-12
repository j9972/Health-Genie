package com.example.healthgenie.domain.user.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "register_condition_tb")
public class RegisterCondition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_condition_id")
    private Long id;

    @Column(name ="requirement")
    private boolean requirement;

    @Column(name = "condition_agree")
    private String conditionAgree;

    @Column(name = "using_privacy_info_agree")
    private String usingPrivacyInfoAgree;

    @Column(name = "email_reception_agree")
    private String emailReceptionAgree;
}
