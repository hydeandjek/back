package com.example.demo.shareapi.entity;

import com.example.demo.userapi.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "shareCommentId")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "share_comment")
public class ShareComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shareCommentId;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime regDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_id")
    private Share share;

//    public static CommentResponseDTO toDTO(Comment comment){
//
//    }




}
