package com.example.healthgenie.domain.community.entity;

import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="COMMUNITY_POST_PHOTO_TB")
@Builder
public class CommunityPostPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_photo_id")
    private Long id;

    @ManyToOne
    @Setter
    @JoinColumn(name = "community_post_id")
    private CommunityPost post;

    // 이미지 경로
    @Column(name = "post_photo_path")
    private String postPhotoPath;
}