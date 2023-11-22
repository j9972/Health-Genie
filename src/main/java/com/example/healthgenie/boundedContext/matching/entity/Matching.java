package com.example.healthgenie.boundedContext.matching.entity;

import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "MATCHING_TB")
public class Matching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_pt_matching_id")
    private Long id;

    @Column(name = "pt_date")
    private Date ptDate;

    @Column(name = "pt_place")
    private String ptPlace;

    @Column(name = "pt_accept")
    private boolean ptAccept;

    @Column(name = "pt_price")
    private int price;

    /*
        [채팅에서] 트레이너가 PT 요청시 회원이 수락 하면 True값으로 변경, 취소하면 default 값 그대로 유지
     */
    @Column(name = "pt_experiecne")
    private boolean ptExperience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="trainer_id")
    private User trainer;

    @Override
    public String toString() {
        return "Matching{" +
                "id=" + id +
                ", ptDate=" + ptDate +
                ", ptPlace='" + ptPlace + '\'' +
                ", ptAccept=" + ptAccept +
                ", price=" + price +
                ", ptExperience=" + ptExperience +
                // Include other fields as needed...
                '}';
    }
}
