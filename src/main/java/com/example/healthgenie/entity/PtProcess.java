package com.example.healthgenie.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "Pt_process_tb")
public class PtProcess extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pt_process_id")
    private Long id;

    @Column(name = "date")
    private String date;

    @Column(name = "pt_times")
    private Long ptTimes;

    @Column(name = "body_state")
    private String bodyState;

    @Column(name ="bmi")
    private String bmi;

    @Column(name = "weakness")
    private String weakness;

    @Column(name = "strength")
    private String strength;

    @Column(name ="pt_comment")
    private String ptComment;

    @Column(name = "pt_start_date")
    private String ptStartDate;

    @Column(name = "pt_left_times")
    private String ptLeftTimes;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private User trainer;






//    @Builder(builderMethodName = "hi")
//    public hihi(User user,String bmi){
//        setUser(user);
//
//    }
//
//    private void setUser(User user){
//        this.user = user;
//        user.addUser(this);
//    }

}
