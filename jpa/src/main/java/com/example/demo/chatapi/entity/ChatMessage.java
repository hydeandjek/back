package com.example.demo.chatapi.entity;

import com.example.demo.userapi.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
//@Entity
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK , ALERT
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "type")
    private MessageType type;

    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();
//
//    @Column(name = "room_id")
//    private Long roomId;
    private String roomId;
//
//    @OneToMany(mappedBy = "id")
//    private User user;
    private String userId;
    private String userName;
//
//    @Column(name = "message")
    private String message;


    public String getDateToString() {
        return DateTimeFormatter.ofPattern("a hh:mm").format(this.date);
    }


}
