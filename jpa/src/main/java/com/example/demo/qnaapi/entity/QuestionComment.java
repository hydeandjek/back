package com.example.demo.qnaapi.entity;

import com.example.demo.userapi.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = "commentId")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "qna_comment")
public class QuestionComment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private QuestionBoard board;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime regDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;







}
