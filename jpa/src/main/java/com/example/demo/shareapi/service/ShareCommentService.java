package com.example.demo.shareapi.service;

import com.example.demo.auth.TokenUserInfo;
import com.example.demo.shareapi.dto.ShareCommentRequestDTO;
import com.example.demo.shareapi.dto.ShareCommentResponseDTO;
import com.example.demo.shareapi.entity.Share;
import com.example.demo.shareapi.entity.ShareComment;
import com.example.demo.shareapi.repository.ShareRepository;
import com.example.demo.shareapi.repository.ShareCommentRepository;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ShareCommentService {

    private final ShareCommentRepository commentRepository;
    private final ShareRepository boardRepository;
    private final UserRepository userRepository;

    public List<ShareCommentResponseDTO> getCommentList(int boardId) {

        List<ShareComment> commentList = commentRepository.findAllByBoardId(boardId);
        log.info("게시글의 댓글 목록 조회: {}", commentList);

        List<ShareCommentResponseDTO> dtoList = new ArrayList<>();
        for(ShareComment comment : commentList){
            ShareCommentResponseDTO dto;
            if(comment.getUpdateDate() != null){
                dto = ShareCommentResponseDTO.builder()
//                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .regDate(comment.getUpdateDate()) // 수정된 댓글이라면 수정날짜 넣기
                        .userId(comment.getUser().getId())
                        .boardId(comment.getShare().getShareId())
                        .build();

            } else {
                dto = ShareCommentResponseDTO.builder()
//                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .regDate(comment.getRegDate()) // 수정안됐다면 등록 날짜 넣기
                        .userId(comment.getUser().getId())
                        .boardId(comment.getShare().getShareId())
                        .build();

            }
            dtoList.add(dto);
        }
        return dtoList;
    }


    public ShareCommentResponseDTO registerComment(int boardId,
                                                   ShareCommentRequestDTO requestDTO,
                                                   TokenUserInfo userInfo) {
        // 등록은 유저만 할 수 있으므로 유저 엔티티 조회해 전달
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        Share board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("대상 게시글이 없어서 댓글 생성 실패!")
        );
        log.info("id가 {}인 게시글에 댓글을 등록한 유저: {}", board.getShareId(), user);

        // 댓글 엔티티 생성 및 등록
        ShareComment saved = commentRepository.save(requestDTO.toEntity(user, board));
        log.info("할 일 저장 완료! saved: {}", saved);

        return ShareCommentResponseDTO.builder()
                .content(saved.getContent())
                .userId(saved.getUser().getId())
                .regDate(saved.getRegDate())
                .boardId(board.getShareId())
                .build();


    }

    public ShareCommentResponseDTO updateComment(ShareCommentRequestDTO requestDTO,
                                                 String userId,
                                                 int boardId,
                                                 int commentId) {

        // dto를 엔티티로 변환 (수정 적용할 것)
        ShareComment entityToBeApplied = requestDTO.toEntity(userRepository.findById(userId).orElseThrow(),
                boardRepository.findById(boardId).orElseThrow());
//        log.info("entityToBeApplied:{}", entityToBeApplied);

        // 타겟 댓글 (수정 대상)
        ShareComment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("대상 댓글이 없어서 댓글 수정 실패!")
        );

        comment.setContent(entityToBeApplied.getContent());
//        log.info("comment:{}", comment);

        ShareComment updated = commentRepository.save(comment);
//        log.info("updated:{}", updated);

        return ShareCommentResponseDTO.builder()
                .content(updated.getContent())
                .userId(updated.getUser().getId())
                .regDate(updated.getRegDate())
                .boardId(updated.getShare().getShareId())
                .build();// dto로 변환 후 리턴
    }


    public ShareCommentResponseDTO deleteBoard(String userId, int commentId) {
        // 조회 및 예외 처리
        ShareComment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("대상이 존재하지 않아 삭제 실패!")
        );
        if(!userId.equals(comment.getUser().getId())){
            log.warn("작성자가 아니므로 삭제 불가");
            throw new RuntimeException("작성 권한이 없습니다.");
        }

        //삭제
        commentRepository.delete(comment);
        //삭제한 엔티티를 dto로 변환
        return ShareCommentResponseDTO.builder()
                .content(comment.getContent())
                .regDate(comment.getRegDate())
                .userId(comment.getUser().getId())
                .boardId(comment.getShare().getShareId())
                .build();
    }
}
