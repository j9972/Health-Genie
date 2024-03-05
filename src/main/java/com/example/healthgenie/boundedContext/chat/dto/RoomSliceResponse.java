package com.example.healthgenie.boundedContext.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RoomSliceResponse {

    private List<RoomQueryResponse> contents;
    private boolean last;

    public static RoomSliceResponse of(Slice<RoomQueryResponse> rooms) {
        return RoomSliceResponse.builder()
                .contents(rooms.getContent())
                .last(rooms.isLast())
                .build();
    }
}
