package com.example.demo.chatapi.dto;

import lombok.*;

@Getter
@ToString @EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ChatMessageListDTO {
    private String userName;
    private String message;
    private String userId;
    private String date;
}
