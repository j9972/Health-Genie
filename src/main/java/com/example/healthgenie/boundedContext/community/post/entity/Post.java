package com.example.healthgenie.boundedContext.community.post.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.community.comment.entity.Comment;
import com.example.healthgenie.boundedContext.community.photo.entity.Photo;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="community_post_id")
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name ="post_content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;

    @Builder.Default
    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    /*
    연관 관계 편의 메서드
     */
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setPost(this);
    }

    public void removePhotos(List<Photo> photos) {
        this.photos.removeAll(photos);
    }
}