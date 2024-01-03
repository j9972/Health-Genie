package com.example.healthgenie.boundedContext.matching.entity;

import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "MATCHING_USER_TB")
public class MatchingUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="matching_user_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "matching_id")
    private Matching matching;
}
