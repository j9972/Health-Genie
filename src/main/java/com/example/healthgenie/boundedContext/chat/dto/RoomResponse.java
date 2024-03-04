package com.example.healthgenie.boundedContext.chat.dto;

import com.example.healthgenie.boundedContext.chat.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {

    private Long roomId;


    public static RoomResponse of(Room room) {
        return RoomResponse.builder()
                .roomId(room.getId())
                .build();
    }

    public static List<RoomResponse> of(List<Room> rooms) {
        return rooms.stream()
                .map(RoomResponse::of)
                .collect(Collectors.toList());
    }
}
