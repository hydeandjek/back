package com.example.demo.chatapi.controller;

import com.example.demo.auth.TokenUserInfo;
import com.example.demo.chatapi.config.AdminRoomId;
import com.example.demo.chatapi.dto.CreateRoomDTO;
import com.example.demo.chatapi.entity.Room;
import com.example.demo.chatapi.exception.ChatMessageListResetException;
import com.example.demo.chatapi.repository.RoomRepository;
import com.example.demo.chatapi.service.RoomService;
import com.example.demo.chatapi.util.SHA256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin
public class RoomController {
    private final RoomService roomService;

    // 사용자의 채팅방 만들기
    @GetMapping("/api/chat/room")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal TokenUserInfo userInfo) {
        log.info("/api/chat/room - user: {}", userInfo);
        Room room = roomService.createRoom(userInfo.getUserId());
        return ResponseEntity.ok().body(new CreateRoomDTO(room));
    }

    // 전체 채팅방 리스트
    @GetMapping("/api/chat/admin/rooms")
    public ResponseEntity<?> listRooms() {

        return ResponseEntity.ok().body(roomService.roomsSimpleList());
    }

    // 개발용
    @GetMapping("/api/chat/admin/rooms/debug")
    public ResponseEntity<?> listRoomAll() {

        return ResponseEntity.ok().body(roomService.roomsList());
    }

    // 관리자용 채팅방 room_id
    @GetMapping("/api/chat/admin/room")
    public ResponseEntity<?> getAdminRoom() {
        log.info("/api/chat/room/admin");
        return ResponseEntity.ok().body(AdminRoomId.ADMIN_ROOM_ID);
    }

    // 사용자용 채팅방 메시지 리스트
    @GetMapping("/api/chat/room/{rid}")
    public ResponseEntity<?> getRoomMessages(@PathVariable("rid") String roomId) {
        log.info("/api/chat/room/: {}", roomId);
        try {
            return ResponseEntity.ok().body(roomService.getMessages(roomId, false));
        } catch (Exception e) {
            log.warn("", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 관리자용 채팅방 메시지 리스트
    @GetMapping("/api/chat/admin/room/{rid}")
    public ResponseEntity<?> adminGetRoomMessages(@PathVariable("rid") String roomId) {
        log.info("/api/chat/room/: {}", roomId);
        try {
            return ResponseEntity.ok().body(roomService.getMessages(roomId, true));
        } catch (Exception e) {
            log.warn("", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 관리자용 채팅방의 읽기 카운트 초기화
    @DeleteMapping("/api/chat/admin/room/{rid}/unread")
    public ResponseEntity<?> adminResetRoomUnreadChat(@PathVariable("rid") String roomId) {
        log.info("/api/chat/room/: {}", roomId);
        try {
            roomService.roomResetUnreadCount(roomId);
            return ResponseEntity.ok().body("ok");
        } catch (Exception e) {
            log.warn("", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }






}
