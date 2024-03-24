package com.example.healthgenie.boundedContext.trainer.profile.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
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
@Table(name = "TRAINER_INFO_TB")
public class TrainerInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_info_id")
    private Long id;

    @Column(name = "name")
    private String name;

    // 아래 4개는 관리페이지에서도 사용
    // 소개글
    @Column(name = "introduction")
    private String introduction;

    // 약력
    @Column(name = "career_history")
    private String career;

    // 경력 [ 개월 수 ]
    @Column(name = "career_month")
    private int careerMonth;

    // 견적
    @Column(name = "cost")
    private int cost;

    @Column(name = "university")
    private String university;

    @Column(name = "contact_start_time")
    private LocalTime startTime;

    @Column(name = "contact_end_time")
    private LocalTime endTime;

    @Column(name = "review_avg")
    private Double reviewAvg;

    // 이 사람이 trainer
    @OneToOne
    @JoinColumn(name = "user_id")
    private User member;

    @Builder.Default
    @OneToMany(mappedBy = "info", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TrainerPhoto> trainerPhotos = new ArrayList<>();

    public void clearProfileImages() {
        this.trainerPhotos.clear();
    }

    public void updateProfileImages(List<TrainerPhoto> profileImages) {
        this.trainerPhotos = profileImages;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updateCost(int cost) {
        this.cost = cost;
    }

    public void updateCareer(String career) {
        this.career = career;
    }

    public void updateMonth(int month) {
        this.careerMonth = month;
    }

    public void updateStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void updateEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void updateUniversity(String university) {
        this.university = university;
    }

    public void updateReviewAvg(Double reviewAvg) {
        this.reviewAvg = reviewAvg;
    }

    public void addPhoto(TrainerPhoto photo) {
        this.trainerPhotos.add(photo);
        photo.setInfo(this);
    }

    public void removePhotos(List<TrainerPhoto> photos) {
        this.trainerPhotos.removeAll(photos);
    }

}
