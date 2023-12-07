package com.example.healthgenie.boundedContext.matching.dto;

import com.example.healthgenie.base.utils.DateUtils;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
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
    private String date;
    private String time;
    private String place;
    private String description;
    private MatchingState state;
    private Long userId;
    private Long trainerId;

    public static MatchingResponse of(Matching matching) {
        LocalDateTime dateTime = matching.getDate();
        String date = DateUtils.toDate(dateTime);
        String time = DateUtils.toTime(dateTime);

        return MatchingResponse.builder()
                .id(matching.getId())
                .date(date)
                .time(time)
                .place(matching.getPlace())
                .description(matching.getDescription())
                .state(matching.getState())
                .userId(matching.getMember().getId())
                .trainerId(matching.getTrainer().getId())
                .build();
    }

    public static List<MatchingResponse> of(List<Matching> matchings) {
        return matchings.stream()
                .map(MatchingResponse::of)
                .toList();
    }
}
