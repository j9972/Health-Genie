package com.example.healthgenie.boundedContext.matching.dto;

import com.example.healthgenie.base.utils.DateUtils;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @Builder.Default
    private List<Long> matchingUsers = new ArrayList<>();

    public static MatchingResponse of(Matching matching) {
        return MatchingResponse.builder()
                .id(matching.getId())
                .date(DateUtils.toStringDate(matching.getDate()))
                .time(DateUtils.toStringTime(matching.getTime()))
                .place(matching.getPlace())
                .description(matching.getDescription())
                .state(matching.getState())
                .matchingUsers(matching.getMatchingUsers().stream().map(e -> e.getUser().getId()).toList())
                .build();
    }

    public static List<MatchingResponse> of(List<Matching> matchings) {
        return matchings.stream()
                .map(MatchingResponse::of)
                .toList();
    }
}
