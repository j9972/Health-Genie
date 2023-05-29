package com.example.healthgenie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "User_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "uniname")
    private String uniName;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "refresh_token_id")
    private String refreshTokenId;

    @Column(name = "name")
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;


    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<PtProcess> ptProcesses;

    public void addUser(PtProcess pt){
        this.ptProcesses.add(pt);
    }
}
