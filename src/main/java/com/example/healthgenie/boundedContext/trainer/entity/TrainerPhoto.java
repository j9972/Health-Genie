package com.example.healthgenie.boundedContext.trainer.entity;

import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "TRAINER_PHOTO_TB")
public class TrainerPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="trainer_photo_id")
    private Long id;

    @ManyToOne
    @Setter
    @JoinColumn(name = "trainer_info_id")
    private TrainerInfo info;

    // 이미지 경로
    @Column(name = "info_photo_path")
    private String infoPhotoPath;
}
