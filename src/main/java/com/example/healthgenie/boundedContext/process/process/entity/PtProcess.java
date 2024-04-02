package com.example.healthgenie.boundedContext.process.process.entity;


import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.process.photo.entity.ProcessPhoto;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private LocalDate date;

    @Column(name = "process_content")
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
    @OneToMany(mappedBy = "process", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProcessPhoto> ptProcessPhotos = new ArrayList<>();


    /*
    연관 관계 편의 메서드
     */
    public void addProcess(ProcessPhoto process) {
        this.ptProcessPhotos.add(process);
        process.setProcess(this);
    }

}