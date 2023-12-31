package com.example.demo.chatapi.dto;

import com.example.demo.chatapi.entity.ChatMessage;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAlertMessageDTO {
    private ChatMessage.MessageType type;
    private String roomId;
    private String message;
}
