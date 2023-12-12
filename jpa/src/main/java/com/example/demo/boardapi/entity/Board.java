package com.example.demo.boardapi.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "boardId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    @GeneratedValue(generator = "system-uuid" ) // 값 지정
    @GenericGenerator(name = "system-uuid", strategy = "uuid") // 커스텀한 값(uuid)
    private String userId;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime regDate;

    private LocalDateTime updateDate;




//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;


}
