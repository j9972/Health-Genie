package com.example.healthgenie.Email.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "email_auth_code")
public class EmailAuthCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="email_auth_code_id")
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name = "auth_code")
    private String code;
}
