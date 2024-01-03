package com.example.demo.boardapi.entity;

import com.example.demo.boardapi.dto.BoardDetailResponseDTO;
import com.example.demo.userapi.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

//    @Column(nullable = false)
//    @GeneratedValue(generator = "system-uuid" ) // 값 지정
//    @GenericGenerator(name = "system-uuid", strategy = "uuid") // 커스텀한 값(uuid)
//    private String userId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime regDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //    @OneToMany(mappedBy = "board",
//            fetch = FetchType.EAGER,
//            cascade = CascadeType.REMOVE)
//    @OrderBy("id asc")
    // 댓글 정렬
//    private List<Comment> comments;
    public BoardDetailResponseDTO toDTO(){
        return null;
    }


}
