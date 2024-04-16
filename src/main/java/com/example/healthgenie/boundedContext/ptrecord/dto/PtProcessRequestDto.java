package com.example.healthgenie.boundedContext.ptrecord.dto;

import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class PtProcessRequestDto {
    //private Long id;
    private LocalDate date; // 내가 작성한 날짜가 필요하다.
    private String title;
    private String content;
    private List<MultipartFile> photos;
    private String userNickName;
    private String trainerNickName;

    public PtProcess toEntity(User user, User currentUser) {
        return PtProcess.builder()
                .date(this.date)
                .title(this.title)
                .content(this.content)
                .member(user)
                .trainer(currentUser)
                .build();
    }
}
