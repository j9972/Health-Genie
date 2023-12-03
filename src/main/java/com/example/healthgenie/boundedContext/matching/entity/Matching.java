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
    private Date date; // LocalDateTime으로 통합 필요 -> 원하는 데이터로 프론트에서 파싱하든 DTO로 반환해서 주든 해야할듯?

    @Column(name = "pt_place")
    private String place;

    @Column(name = "pt_time")
    private String time; // LocalDateTime으로 통합 필요

    @Column(name = "pt_accept")
    private boolean ptAccept; // 필요할까? PT가 수락 됐을 경우에만 매칭 데이터를 생성하는게 맞지 않나? 애초에 거절한 내역을 DB에 저장할 필요가 없어보임. 만약 요구사항이 미래에 거절한 내역이 필요할 것 같아서 추가했다 라고 한다면 필요 -> 그렇다면 변수 네이밍 수정 ex: isAccepted

    @Column(name = "pt_price")
    private int price; // PT 가격은 어떻게 결정되는가? 애초부터 가격정찰제로 딱 정해놓던지, 채팅방에서 트레이너가 매칭 요청을 보낼 때 채팅으로 협의를 끝마친 가격으로 보일 수 있도록 디자인 수정이 필요하지 않을까?

    /*
        [채팅에서] 트레이너가 PT 요청시 회원이 수락 하면 True값으로 변경, 취소하면 default 값 그대로 유지
     */
    @Column(name = "pt_experiecne")
    private boolean expericence; // ptAccept와 같은 맥락으로 고민해봐야할듯?

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User member;

    @ManyToOne
    @JoinColumn(name ="trainer_id")
    private User trainer;

    // PT 매칭 데이터에 "내용" 필드가 추가되어야 할 것 같음 -> 디자인에는 "내용" 필드 존재


    @Override
    public String toString() {
        return "Matching{" +
                "id=" + id +
                ", date=" + date +
                ", place='" + place + '\'' +
                ", time=" + time +
                ", ptAccept=" + ptAccept +
                ", price=" + price +
                ", ptExperience=" + expericence +
                // Include other fields as needed...
                '}';
    }
}
