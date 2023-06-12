package com.example.healthgenie.domain.community.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "community_post_tb")
public class CommunityPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="community_post_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name ="body")
    private String body;

    @Column(name = "like_count")
    private Long likeCount;

    @Lob
    private Blob pics;

    @OneToMany
    @JoinColumn(name = "community_comment_id")
    private List<CommunityComment> commentList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
