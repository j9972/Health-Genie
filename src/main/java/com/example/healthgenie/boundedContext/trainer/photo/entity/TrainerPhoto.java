package com.example.healthgenie.boundedContext.trainer.photo.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "TRAINER_PHOTO_TB")
public class TrainerPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_photo_id")
    private Long id;

    @ManyToOne
    @Setter
    @JoinColumn(name = "trainer_info_id")
    private TrainerInfo info;

    // 이미지 경로
    @Column(name = "info_photo_path", columnDefinition = "TEXT")
    private String infoPhotoPath;

    @Column(name = "original_file_name")
    private String name;
}
