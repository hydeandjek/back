package com.example.demo.chatapi.dto;

import com.example.demo.chatapi.entity.Room;
import lombok.*;

import java.util.UUID;


@Getter
@ToString @EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class CreateRoomDTO {
    private String roomId;

    public CreateRoomDTO(Room room) {
        this.roomId = room.getRoomId();
    }
}
