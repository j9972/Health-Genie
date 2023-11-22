package com.example.healthgenie.boundedContext.ptrecord.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="PT_PROCESS_PHOTO_TB")
@Builder
public class PtProcessPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pt_process_photo_id")
    private Long id;

    @ManyToOne
    @Setter
    @JoinColumn(name = "pt_process_id")
    private PtProcess process;

    // 이미지 경로
    @Column(name = "process_photo_path")
    private String processPhotoPath;
}