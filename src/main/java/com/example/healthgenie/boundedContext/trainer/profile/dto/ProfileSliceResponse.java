package com.example.healthgenie.boundedContext.trainer.profile.dto;

import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
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
public class ProfileSliceResponse {

    private List<ProfileResponseDto> infos;
    private boolean last;

    public static ProfileSliceResponse of(Slice<TrainerInfo> info) {
        return ProfileSliceResponse.builder()
                .infos(ProfileResponseDto.of(info.getContent()))
                .last(info.isLast())
                .build();
    }

}