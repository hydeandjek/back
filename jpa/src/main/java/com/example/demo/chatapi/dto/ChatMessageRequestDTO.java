package com.example.demo.chatapi.dto;

import com.example.demo.chatapi.entity.ChatMessage;
import lombok.*;

@Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChatMessageRequestDTO {

    private ChatMessage.MessageType type;
    private String roomId;
    private String userName;
    private String token;
    private String message;
}
