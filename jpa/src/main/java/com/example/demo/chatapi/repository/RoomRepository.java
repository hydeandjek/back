package com.example.demo.chatapi.repository;

import com.example.demo.chatapi.entity.ChatMessage;
import com.example.demo.chatapi.entity.Room;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class RoomRepository {

    private final List<Room> rooms = new LinkedList<>();

    public Room createRoom(String userId, String userName) {
        Room room = Room.builder()
                .userId(userId)
                .userName(userName)
                .unreadCount(0)
                .build();
        rooms.add(room);
        return room;
    }

    public List<String> getRoomsIdList() {
        return rooms.stream()
                .map(Room::getRoomId)
                .collect(Collectors.toList());
    }

    public List<Room> getRoomList() {
        return rooms;
    }

    public void addMessage(ChatMessage chatMessage, String roomId) {
        log.info(roomId);
        Room room = getRoom(roomId);
        int i = rooms.indexOf(room);
        room.getChatMessageList().add(chatMessage);
        room.setUnreadCount(room.getUnreadCount() + 1);
        rooms.set(i, room);
        log.info(rooms.toString());
    }

    public List<ChatMessage> getMessageList (String roomId) {
        return getRoom(roomId).getChatMessageList();
    }

    private Room getRoom(String roomId) {
        return rooms.stream()
            .filter(r -> r.getRoomId().equals(roomId))
            .findFirst()
            .orElseThrow();
    }

    public void clearMessageList(String roomId) {
        Room room = getRoom(roomId);
        int i = rooms.indexOf(room);
        room.getChatMessageList().clear();
        rooms.set(i, room);
    }

    public void resetRoomUnreadCount(String roomId) {
        Room room = getRoom(roomId);
        int i = rooms.indexOf(room);
        room.setUnreadCount(0);
        rooms.set(i, room);
    }

}
