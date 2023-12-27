package com.example.demo.qnaapi.service;


import com.example.demo.auth.TokenUserInfo;
import com.example.demo.qnaapi.dto.request.BoardCreateRequestDTO;
import com.example.demo.qnaapi.dto.request.BoardModifyRequestDTO;
import com.example.demo.qnaapi.dto.request.CommentCreateRequestDTO;
import com.example.demo.qnaapi.dto.request.CommentModifyRequestDTO;
import com.example.demo.qnaapi.dto.response.BoardDetailResponseDTO;
import com.example.demo.qnaapi.dto.response.CommentDetailResponseDTO;
import com.example.demo.qnaapi.entity.QuestionBoard;
import com.example.demo.qnaapi.entity.QuestionComment;

import com.example.demo.qnaapi.repository.QnaBoardRepository;
import com.example.demo.qnaapi.repository.QnaCommentRepository;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor // @RequiredArgsConstructor를 사용할 땐 레포지토리에 final을 해야 주입이 된다
@Transactional
public class QnaService {
    private final QnaBoardRepository boardRepository;
    private final UserRepository userRepository;
    private final QnaCommentRepository commentRepository;




    // 모든 게시물 정보
    public List<BoardDetailResponseDTO> getBoardList() {

        List<QuestionBoard> all = boardRepository.findAll();

        log.info(all.toString());
        List<BoardDetailResponseDTO> boardDetailList = new ArrayList<>();

        for (int i = 0; i < all.size(); i++) {
            QuestionBoard questionBoard = all.get(i);

            BoardDetailResponseDTO build = BoardDetailResponseDTO.builder()
                    .rowNumber(i + 1) // i는 0부터 시작하므로 1을 더해줌
                    .boardId(questionBoard.getBoardId())
                    .title(questionBoard.getTitle())
                    .content(questionBoard.getContent())
                    .regDate(questionBoard.getRegDate())
                    .updateDate(questionBoard.getUpdateDate())
                    .userId(questionBoard.getUser().getId())
                    .userName(questionBoard.getUser().getUserName())
                    .build();

            boardDetailList.add(build);
        }

        log.info(boardDetailList.toString());


        return boardDetailList;
    }


    // 클릭 게시물 상세 정보
    public BoardDetailResponseDTO getboard(int id) {
        QuestionBoard allById = boardRepository.findById(id);
        log.info("yyyyyyyyyyy{}",allById);

        String id1 = allById.getUser().getId();

        BoardDetailResponseDTO build = BoardDetailResponseDTO.builder()
                .boardId(id)
                .title(allById.getTitle())
                .content(allById.getContent())
                .regDate(allById.getRegDate())
                .regDate(allById.getUpdateDate())
                .userId(id1)
                .userName(allById.getUser().getUserName())
                .build();


        log.info("yyyyyyyyyyy{}",build);

        return build;

    }

    // 게시물 추가
    public QuestionBoard addboard(BoardCreateRequestDTO requestDTO, TokenUserInfo userInfo) {
        // 게시글 등록은 유저만 할 수 있으므로 유저 엔티티 조회해 전달
        User user = getUser(userInfo.getUserId());
        log.info(String.valueOf(user));

        QuestionBoard save = boardRepository.save(requestDTO.toEntity(user));

        // 등록
        log.info("할 일 저장 완료! 제목: {}", requestDTO.getTitle());

        return save;

    }


    // 유저 조회 메서드
    private User getUser(String userId) {
        log.info(userId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );

        return user;
    }

    // 질문게시판 수정
    public BoardDetailResponseDTO updateboard(BoardModifyRequestDTO requestDTO, int id, TokenUserInfo userInfo) {

        QuestionBoard allById = boardRepository.findById(id);

        User user = getUser(userInfo.getUserId());

        if (allById.getUser().getId().equals(user.getId())){
            QuestionBoard save = boardRepository.save(requestDTO.toEntity(user, id, allById.getRegDate()));

            BoardDetailResponseDTO dto = BoardDetailResponseDTO.builder()
                    .userId(userInfo.getUserId())
                    .updateDate(LocalDateTime.now())
                    .boardId(id)
                    .content(save.getContent())
                    .regDate(allById.getRegDate())
                    .title(requestDTO.getTitle())
                    .build();
            log.info("게시글 수정 완료!");
            return dto;
        }else{
            return null;
        }

    }


    // 게시물 삭제
    public void deleteboard(int id, String userId) {

        QuestionBoard allById = boardRepository.findById(id);

        log.info(allById.toString());
        log.info(userId);

        if (allById.getUser().getId().equals(userId)){
            log.info("성공성공성공");
            boardRepository.deleteById(id);
        }
    }


    /************************** 댓글 요청 처리 ***************************/


    // 게시글 댓글 리스트
    public List<CommentDetailResponseDTO> getboardcomment(int boardId) {

        List<QuestionComment> findboard = commentRepository.findboard(boardId);


        log.info(findboard.toString());

        List<CommentDetailResponseDTO> dtoList = new ArrayList<>();
        for(QuestionComment comment : findboard){
            CommentDetailResponseDTO dto;
            if(comment.getUpdateDate() != null){
                dto = CommentDetailResponseDTO.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .regDate(comment.getUpdateDate()) // 수정된 댓글이라면 수정날짜 넣기
                        .userId(comment.getUser().getId())
                        .boardId(comment.getBoard().getBoardId())
                        .userName(comment.getUser().getUserName())
                        .build();

            } else {
                dto = CommentDetailResponseDTO.builder()
//                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .regDate(comment.getRegDate()) // 수정안됐다면 등록 날짜 넣기
                        .userId(comment.getUser().getId())
                        .boardId(comment.getBoard().getBoardId())
                        .build();

            }
            dtoList.add(dto);
            log.info("zzzzz{}",dtoList.toString());
        }
        return dtoList;
    }




    // 게시물 댓글 추가
    public CommentDetailResponseDTO addcomment(CommentCreateRequestDTO requestDTO, TokenUserInfo userInfo, int boardId) {
        User user = getUser(userInfo.getUserId());
        log.info(String.valueOf(user));

        QuestionBoard byId = boardRepository.findById(boardId);

        QuestionComment entity = requestDTO.toEntity(user, byId);
        commentRepository.save(entity);


        CommentDetailResponseDTO entity1;
        entity1 = CommentDetailResponseDTO.builder()
                .boardId(boardId)
                .userId(userInfo.getUserId())
                .commentId(entity.getCommentId())
                .content(requestDTO.getContent())
                .regDate(entity.getRegDate())
                .build();



        log.info("게시물 댓글 저장 완료! 내용: {}", requestDTO.getContent());

        return entity1;
    }

    // 댓글 수정
    public CommentDetailResponseDTO updateComment(CommentModifyRequestDTO requestDTO, String userId, int boardId, int commentId) {

        User user = getUser(userId);
        QuestionBoard byboardId = boardRepository.findById(boardId);
        QuestionComment bycommentId = commentRepository.findById(commentId);

        if (bycommentId.getBoard().getBoardId() == boardId){
            QuestionComment entity = requestDTO.toEntity(bycommentId);

            commentRepository.save(entity);

            CommentDetailResponseDTO entity1;
            entity1 = CommentDetailResponseDTO.builder()
                    .boardId(bycommentId.getBoard().getBoardId())
                    .userId(bycommentId.getUser().getId())
                    .commentId(bycommentId.getCommentId())
                    .content(requestDTO.getContent())
                    .regDate(bycommentId.getRegDate())
                    .updateDate(LocalDateTime.now())
                    .build();

            log.info("게시물 댓글 수정 완료! 내용: {}", entity1);
            return entity1;
        }else {
            return null;
        }

    }


    // 댓글 삭제
    public void boardCommentDel(TokenUserInfo userInfo, int boardId, int commentId) {

        QuestionComment bycommentId = commentRepository.findById(commentId);

        log.info(userInfo.toString());
        log.info(String.valueOf(boardId));
        log.info(String.valueOf(commentId));

        log.info(bycommentId.toString());

        if (bycommentId.getUser().getId().equals(userInfo.getUserId())){
            commentRepository.deleteById(commentId);
        }

        log.info("게시물 댓글 삭제 완료!");
    }
}
