package com.example.healthgenie.domain.community.entity;


import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "community_comment_tb")
public class CommunityComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_comment_id")
    private Long id;

    @Column(name ="comment_body")
    private String commentBody;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User member;

}
