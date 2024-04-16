package com.example.healthgenie.boundedContext.ptrecord.dto;

import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
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
public class PtProcessSliceResponse {

    private List<PtProcessResponseDto> contents;
    private boolean last;

    public static PtProcessSliceResponse of(Slice<PtProcess> process) {
        return PtProcessSliceResponse.builder()
                .contents(PtProcessResponseDto.of(process.getContent()))
                .last(process.isLast())
                .build();
    }

}
