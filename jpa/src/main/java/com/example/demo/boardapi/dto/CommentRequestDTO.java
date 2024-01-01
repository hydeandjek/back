package com.example.demo.boardapi.dto;

import com.example.demo.boardapi.entity.Board;
import com.example.demo.boardapi.entity.Comment;
import com.example.demo.boardapi.repository.BoardRepository;
import com.example.demo.userapi.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {

    private String content;


    // dto를 엔티티로 변환
    public Comment toEntity(User user, Board board){
        return Comment.builder()
                .content(this.content)
                .user(user)
                .board(board)
                .build();
    }




}
