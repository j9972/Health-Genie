package com.example.healthgenie.boundedContext.user.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.boundedContext.community.entity.CommunityComment;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

import static com.example.healthgenie.base.exception.UserErrorResult.ALREADY_EXISTS_ROLE;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_TB")
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

    @NotNull
    @Size(min = 2, max = 8)
    @Column(name = "nickname")
    private String nickname;

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

    // level field 추
    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Matching> match_user= new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<PtProcess> process_user = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<PtReview> reivew_user = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<Todo> todo = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "writer",fetch = FetchType.LAZY )
    private List<CommunityPost> communityPosts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "writer",fetch = FetchType.LAZY )
    private List<CommunityComment> communityComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY )
    private List<Routine> routine = new ArrayList<>();

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
            throw new UserException(ALREADY_EXISTS_ROLE);
        }
        this.role = role;
    }

    public void updateLevel(Level level) {
        this.level = level;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}