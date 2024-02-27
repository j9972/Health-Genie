package com.example.healthgenie.boundedContext.community.entity;


import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@Table(name = "COMMUNITY_COMMENT_TB")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_comment_id")
    private Long id;

    @NotBlank
    @Column(name ="comment_body")
    private String commentBody;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    public void changeContent(String content) {
        this.commentBody = content;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}