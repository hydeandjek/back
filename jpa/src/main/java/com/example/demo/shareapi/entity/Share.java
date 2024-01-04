package com.example.demo.shareapi.entity;

import com.example.demo.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "shareId")
@NoArgsConstructor
@AllArgsConstructor

@Builder
@Entity
@Table(name = "share_board")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shareId;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "share",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    ) // 상대방 엔티티의 조인되는 필드명
    @JsonIgnore
//    @Builder.Default
    private List<Images> uploadImages = new ArrayList<>();
//    private List<String> uploadImages; // 첨부되는 이미지 경로
//    private List<MultipartFile> uploadImages;

    @CreationTimestamp
//    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm", timezone="Asia/Seoul") //날짜 포멧 바꾸기
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime regDate;

    //    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String approvalDate; // 승인 또는 거부 날짜

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //    @Column(nullable = false)
//    private boolean approvalFlag; // 승인여부 (프론트에서 유즈스테이트 디폴트 설정)
    @Enumerated(EnumType.STRING) // enum 타입으로 매핑
    @Column(nullable = false)
    @Builder.Default
    private ApprovalStatus approvalFlag = ApprovalStatus.HOLD; // 승인 여부



    // 나눔 게시글과 양방향 관계 설정
    @OneToMany(mappedBy = "share", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    // 게시글 UI에서 댓글을 바로 보여주기 위해 FetchType을 EAGER
//    // 게시글이 삭제되면 댓글 또한 삭제되어야 하기 때문에 CascadeType.REMOVE
//    @OrderBy("id asc") // 댓글 정렬
    private List<ShareComment> comments;
//    private int commentCount; // 댓글 수




    // 생성자
    public Share(User user, String title, String content){
        this.user = user;
        this.title = title;
        this.content = content;
    }

    // 메서드
    public void updateShare(String title, String content){
        this.title = title;
        this.content = content;
    }
    public String datePicker(String s){
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return currentDate.format(formatter);
//        share.setApprovalDate(formattedDate);
    }


    // Share에서 파일 처리 위함
//    public void addImages(Images images) {
//        this.uploadImages.add(images);
//
//        // 게시글에 파일이 저장되어있지 않은 경우
//        if(images.getShare() != this)
//            // 파일 저장
//            images.setShare(this);
//    }



}