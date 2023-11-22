package com.example.healthgenie.boundedContext.ptrecord.entity;

import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @Builder.Default
    @OneToMany(mappedBy = "process",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<PtProcessPhoto> ptProcessPhotos = new ArrayList<>();

    /*
    연관 관계 편의 메서드
     */
    public void addProcess(PtProcessPhoto process) {
        this.ptProcessPhotos.add(process);
        process.setProcess(this);
    }

}