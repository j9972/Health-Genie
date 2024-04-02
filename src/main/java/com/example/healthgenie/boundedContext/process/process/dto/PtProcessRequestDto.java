package com.example.healthgenie.boundedContext.process.process.dto;

import com.example.healthgenie.boundedContext.process.process.entity.PtProcess;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtProcessRequestDto {

    private LocalDate date; // 내가 작성한 날짜가 필요하다.
    @NotBlank
    private String title;
    private String content;
    @NotBlank
    private String userNickName;
    @NotBlank
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
