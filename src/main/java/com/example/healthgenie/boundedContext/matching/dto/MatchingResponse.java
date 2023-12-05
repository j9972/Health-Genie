package com.example.healthgenie.boundedContext.matching.dto;

import com.example.healthgenie.boundedContext.matching.entity.Matching;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingResponse {

    private Long id;
    private LocalDateTime date;
    private String place;
    private String userNickname;
    private String trainerNickname;

    public static MatchingResponse of(Matching matching) {
        return MatchingResponse.builder()
                .id(matching.getId())
                .date(matching.getDate())
                .place(matching.getPlace())
                .userNickname(matching.getMember().getNickname())
                .trainerNickname(matching.getTrainer().getNickname())
                .build();
    }

    public static List<MatchingResponse> of(List<Matching> matchings) {
        return matchings.stream()
                .map(MatchingResponse::of)
                .toList();
    }
}
