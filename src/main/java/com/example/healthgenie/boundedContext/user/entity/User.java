package com.example.healthgenie.boundedContext.user.entity;

import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.routine.entity.OwnRoutine;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import com.example.healthgenie.boundedContext.community.entity.CommunityComment;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.healthgenie.base.exception.CommonErrorResult.ALREADY_EXISTS_ROLE;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "User_tb", columnNames = {"user_id"})
})
@Builder(toBuilder = true)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique=true)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "uniname")
    private String uniName;

    @NotNull
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProvider authProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "email_verify")
    private boolean emailVerify;

    // 다른 메서드나 로직에서 추가적인 초기화 필요 없기에 아래 OneToMany 코드들을 초기화 하지 않음
    // trainerInfo, RefreshToken, userProfile 부분 oneToOne 매핑 스킵 -> 한쪽에서만 참조하기에!
    // user 2번 참조한 데이터 ( chatMessage, mathcing, PT_PROCESS_TB, PtReivew ) -> 한번만 하면 된다
    @Builder.Default
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<ChatMessage> chatMessages_member = new ArrayList<>(); // NullPointerException을 방지하기 위해서 초기화 해놓음

//    @OneToMany(mappedBy = "trainer",fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
//    private List<ChatMessage> chatMessages_trainer;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Matching> match_user= new ArrayList<>();

//    @OneToMany(mappedBy = "trainer",fetch = FetchType.LAZY )
//    private List<matching> match_trainer;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<PtProcess> process_user = new ArrayList<>();

//    @OneToMany(mappedBy = "trainer",fetch = FetchType.LAZY )
//    private List<PtProcess> process_trainer;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<PtReview> reivew_user = new ArrayList<>();

//    @OneToMany(mappedBy = "trainer",fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
//    private List<PtReivew> reivew_trainer;

    @Builder.Default
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<Todo> todo = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "trainer",fetch = FetchType.LAZY )
    private List<TrainerPhoto> trainerPhotoList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<CommunityPost> communityPosts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<CommunityComment> communityComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<OwnRoutine> own_routine = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getCode()));
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateRole(Role role) {
        if(this.role != Role.EMPTY) {
            throw new CommonException(ALREADY_EXISTS_ROLE);
        }
        this.role = role;
    }
}