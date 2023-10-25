package com.example.healthgenie.domain.ptrecord.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "PT_PROCESS_TB")
public class PtProcess extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pt_process_id")
    private Long id;

    // 자동으로 시간을 저장 안하는 이유는 일지 작성 날짜를 내가 설정할 수 있게 해야하기 때문
    @Column(name = "pt_process_date")
    private String date;

    @Column(name ="process_content")
    private String content;

    @Column(name = "process_title")
    private String title;

    @Column(name = "pt_process_photo")
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User member;


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "trainer_id")
    private User trainer;

}