package com.example.healthgenie.boundedContext.community.photo.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="COMMUNITY_POST_PHOTO_TB")
@Builder
public class Photo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_photo_id")
    private Long id;

    @ManyToOne
    @Setter
    @JoinColumn(name = "community_post_id")
    private Post post;

    @Column(name = "post_photo_path")
    private String path;

    @Column(name = "original_file_name")
    private String name;
}