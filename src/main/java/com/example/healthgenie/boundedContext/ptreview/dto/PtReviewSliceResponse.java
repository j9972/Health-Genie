package com.example.healthgenie.boundedContext.ptreview.dto;


import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PtReviewSliceResponse {

    private List<PtReviewResponseDto> contents;
    private boolean last;

    public static PtReviewSliceResponse of(Slice<PtReview> review) {
        return PtReviewSliceResponse.builder()
                .contents(PtReviewResponseDto.of(review.getContent()))
                .last(review.isLast())
                .build();
    }

}
