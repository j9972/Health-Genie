package com.example.healthgenie.boundedContext.community.entity;

import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.base.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "COMMUNITY_POST_TB")
@Builder(toBuilder = true)
public class CommunityPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="community_post_id")
    private Long id;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name ="post_content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;

    @Builder.Default
    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CommunityComment> communityComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CommunityPostPhoto> communityPostPhotos = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    /*
    연관 관계 편의 메서드
     */
    public void addComment(CommunityComment comment) {
        this.communityComments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(CommunityComment comment) {
        this.communityComments.remove(comment);
    }

    public void addPhoto(CommunityPostPhoto photo) {
        this.communityPostPhotos.add(photo);
        photo.setPost(this);
    }

    public void removePhotos(List<CommunityPostPhoto> photos) {
        this.communityPostPhotos.removeAll(photos);
    }
}