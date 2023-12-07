package com.example.healthgenie.boundedContext.todo.entity;


import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "TODO_TB")
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="todo_id")
    private Long id;

    // LocalDateTime -> 특정 날짜와 시간대 설정 , 브라우저에서 정렬 조건을 나누기 위해서 날짜 시간대를 나눔
    @Column(name = "todo_date")
    private LocalDate date;

    @Column(name = "todo_time")
    private LocalTime time;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    // 관리 페이지에 오늘 pt 있음 없음을 알려주는 flag
    @Column(name = "pt")
    private boolean pt;

    @Enumerated(EnumType.STRING) // 안써주면 숫자로 DB에 반영된다.
    @Column(name = "status")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User member;

    public void updateTitle(String content) {
        this.title = content;
    }
    public void updateDescription(String reason) {
        this.description = reason;
    }
    public void updateStatus(Status status) {
        this.status = status;
    }
    public void updateDate(LocalDate date) {
        this.date = date;
    }
    public void updateTime(LocalTime time) {
        this.time = time;
    }
    public void updatePt(boolean pt) {
        this.pt = pt;
    }

}
