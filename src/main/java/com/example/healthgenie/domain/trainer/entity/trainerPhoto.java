package com.example.healthgenie.domain.trainer.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "TRAINER_PHOTO_TB")
public class TrainerPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="trainer_photo_id")
    private Long id;

    @Column(name = "filename")
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="trainer_id")
    private User trainer;

    public void changeFileName(String filename){
        this.filename = filename;
    }

    public static TrainerPhoto fileToTrainerPhoto(User info,String filename){
        return TrainerPhoto.builder()
                .trainer(info)
                .filename(filename)
                .build();
    }
}
