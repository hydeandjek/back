package com.example.demo.chatapi.service;

import com.example.demo.chatapi.config.AdminRoomId;
import com.example.demo.chatapi.dto.AdminAlertMessageDTO;
import com.example.demo.chatapi.dto.ChatMessageListDTO;
import com.example.demo.chatapi.dto.RoomListDTO;
import com.example.demo.chatapi.entity.ChatMessage;
import com.example.demo.chatapi.entity.Room;
import com.example.demo.chatapi.exception.ChatMessageListResetException;
import com.example.demo.chatapi.repository.RoomRepository;
import com.example.demo.chatapi.util.SHA256;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final SimpMessageSendingOperations sendingOperations;

    public Room createRoom(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.createRoom(user.getId(), user.getUserName());

        AdminAlertMessageDTO adminAlertMessageDTO = AdminAlertMessageDTO.builder()
                .type(ChatMessage.MessageType.ALERT)
                .roomId(room.getRoomId())
                .message("create room")
                .build();

        sendingOperations.convertAndSend("/topic/chat/room/" + AdminRoomId.ADMIN_ROOM_ID, adminAlertMessageDTO);

        return room;
    }

    public List<String> roomsIdList() {
        return roomRepository.getRoomsIdList();
    }

    public List<Room> roomsList() {
        return roomRepository.getRoomList();
    }

    public List<ChatMessageListDTO> getMessages(String roomId, boolean isAdmin) {
        List<ChatMessage> messageList = null;

        try {
            messageList = roomRepository.getMessageList(roomId);
        } catch (Exception e) {
            throw new RuntimeException("방이 없습니다.");
        }

        if (messageList.isEmpty()) return new ArrayList<>();
        ChatMessage lastMessage = ((LinkedList<ChatMessage>) messageList).getLast();
        LocalDateTime date = lastMessage.getDate();

        long diff = ChronoUnit.MINUTES.between(date, LocalDateTime.now());
        if (diff >= 60) {
            log.info("채팅방 초기화");
            roomRepository.clearMessageList(roomId);

            throw new ChatMessageListResetException("채팅방이 오래되었습니다");
        }

        if (isAdmin) {
            roomRepository.resetRoomUnreadCount(roomId);
        }

        return messageList.stream()
                .map(chat -> ChatMessageListDTO.builder()
                        .userName(chat.getUserName())
                        .userId(SHA256.encrypt(chat.getUserId()))
                        .message(chat.getMessage())
                        .date(chat.getDateToString())
                        .build())
                .collect(Collectors.toList());
//        try {
//
//        } catch (Exception e) {
//            log.warn("{}\n{}", e.getMessage(), e.getStackTrace()[0]);
//
//        }

    }

    public List<RoomListDTO> roomsSimpleList() {
        return roomRepository.getRoomList().stream()
                .map((room) -> {
                    String message = "";
                    List<ChatMessage> list = room.getChatMessageList();
                    if (!list.isEmpty()) {
                        message = ((LinkedList<ChatMessage>)list).getLast().getMessage();
                    }
                    return RoomListDTO.builder()
                            .roomId(room.getRoomId())
                            .userName(room.getUserName())
                            .message(message)
                            .unreadCount(room.getUnreadCount())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void roomResetUnreadCount(String roomId) {
        roomRepository.resetRoomUnreadCount(roomId);
    }
}
