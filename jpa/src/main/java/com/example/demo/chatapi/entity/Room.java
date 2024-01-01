package com.example.demo.chatapi.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString @EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Room {
    @Builder.Default
    private String roomId= UUID.randomUUID().toString().replace("-", "");

    private String userName;
    private String userId;

    @Setter
    private long unreadCount;

    @Setter
    @Builder.Default
    private List<ChatMessage> chatMessageList = new LinkedList<>();
}
