package com.example.demo.chatapi.dto;

import com.example.demo.chatapi.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponseDTO {

    private ChatMessage.MessageType type;
    private String roomId;
    private String userId;
    private String userName;
    private String message;
    private String date;

}
