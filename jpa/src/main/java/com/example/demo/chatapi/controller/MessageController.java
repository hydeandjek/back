package com.example.demo.chatapi.controller;

import com.example.demo.auth.TokenProvider;
import com.example.demo.auth.TokenUserInfo;
import com.example.demo.chatapi.config.AdminRoomId;
import com.example.demo.chatapi.dto.AdminAlertMessageDTO;
import com.example.demo.chatapi.dto.ChatMessageRequestDTO;
import com.example.demo.chatapi.dto.ChatMessageResponseDTO;
import com.example.demo.chatapi.entity.ChatMessage;
import com.example.demo.chatapi.entity.Room;
import com.example.demo.chatapi.repository.RoomRepository;
import com.example.demo.chatapi.util.SHA256;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final TokenProvider tokenProvider;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;


    @MessageMapping("/topic/chat/message")
    public void enter(ChatMessageRequestDTO messageDTO, SimpMessageHeaderAccessor accessor) {

        //TokenUserInfo tokenUserInfo = tokenProvider.validateAndGetTokenUserInfo(messageDTO.getToken());
        String token = accessor.getNativeHeader("Authorization").get(0);
        log.info(token);
        TokenUserInfo tokenUserInfo = tokenProvider.validateAndGetTokenUserInfo(token);
        String roomId = messageDTO.getRoomId();
        String userId = tokenUserInfo.getUserId();
        User user = userRepository.findById(userId).orElseThrow();
        String userName = user.getUserName();
        String message = messageDTO.getMessage();


        log.info("/chat/message - roomId: {}, userId: {}, message: {}", roomId, userName, message);


        ChatMessage chatMessage = ChatMessage.builder()
                .message(message)
                .type(messageDTO.getType())
                .roomId(roomId)
                .userId(userId)
                .userName(userName)
                .build();

        try {
            roomRepository.addMessage(chatMessage, roomId);
        } catch (Exception e) {
            log.info("/topic/chat/message", e);
        }

        ChatMessageResponseDTO responseDTO = ChatMessageResponseDTO.builder()
                .roomId(chatMessage.getRoomId())
                .type(messageDTO.getType())
                .userName(chatMessage.getUserName())
                .userId(new SHA256().encrypt(userId))
                .message(chatMessage.getMessage())
                .date(chatMessage.getDateToString())
                .build();

        AdminAlertMessageDTO adminAlertMessageDTO = AdminAlertMessageDTO.builder()
                .type(ChatMessage.MessageType.ALERT)
                .roomId(chatMessage.getRoomId())
                .message("recive message")
                .build();

        sendingOperations.convertAndSend("/topic/chat/room/" + AdminRoomId.ADMIN_ROOM_ID, adminAlertMessageDTO);
        sendingOperations.convertAndSend("/topic/chat/room/" + roomId, responseDTO);
    }
}
