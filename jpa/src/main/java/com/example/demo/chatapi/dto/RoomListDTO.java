package com.example.demo.chatapi.dto;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomListDTO {
    private String roomId;
    private String userName;
    private String message;
    private long unreadCount;

}
