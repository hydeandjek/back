package com.example.demo.shareapi.entity;

import com.example.demo.boardapi.entity.Board;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "imagesId")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "images")
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imagesId;

    @Column(nullable = false)
    private String uploadedFileName; // 사용자 지정 파일 이름

    @Column(nullable = false)
    private String storedFileName; // 서버에 저장되는 파일 이름

    @Column(nullable = false)
    private String filePath; // 서버에 저장되는 파일 저장 경로

    private Long size; // 파일 사이즈

//    private String extension; // 확장자

    @CreationTimestamp
    private LocalDateTime regDate;

//    @UpdateTimestamp
//    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_id")
    @JsonIgnore
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Share share;

    // 생성자
    public Images(String uploadedFileName, String storedFileName, String filePath,
                  Long size){
        this.uploadedFileName = uploadedFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.size = size;
//        this.extension = extension;
    }

    // 게시글 정보 저장하는 메서드
    public void setShare(Share share){
        this.share = share; // 이미지에 대해 share_id 연결
        System.out.println("share = " + share);

//        if(!share.getUploadImages().contains(this)){ // 게시글(의 필드인 이미지 리스트)에 현재 파일이 존재하지 않는다면
//            share.getUploadImages().add(this); // 이미지 리스트에 넣는다.
//
//        }
    }

}
