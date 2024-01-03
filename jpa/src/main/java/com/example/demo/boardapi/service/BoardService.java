package com.example.demo.boardapi.service;

import com.example.demo.boardapi.dto.*;
import com.example.demo.boardapi.entity.Board;
import com.example.demo.boardapi.repository.BoardRepository;
import com.example.demo.qnaapi.entity.QuestionBoard;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.auth.TokenUserInfo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<BoardResponseDTO> getBoardList(String category) {
//        List<Board> list = BoardRepository.findAll(Sort.by(Sort.Direction.DESC, "boardid"));

        List<BoardResponseDTO> dtoList = new ArrayList<>();
        int i = 0;

        if("entire".equals(category)){
            List<Board> all = boardRepository.findAll(Sort.by("regDate").descending());
            int count = all.size();
            for (Board board : all) {
                BoardResponseDTO dto = BoardResponseDTO.builder()
                        .id(board.getBoardId())
                        .title(board.getTitle())
                        .category(board.getCategory())
                        .regDate(board.getRegDate())
                        .userId(board.getUser().getId())
                        .userName(board.getUser().getUserName())
                        .rowNum(count--)
                        .build();

                dtoList.add(dto);
            }

        }else {
            List<Board> boardList = boardRepository.findAllByCategory(category);
            int count = boardList.size();
            for (Board board : boardList) {
                BoardResponseDTO dto = BoardResponseDTO.builder()
                        .id(board.getBoardId())
                        .title(board.getTitle())
                        .category(category)
                        .regDate(board.getRegDate())
                        .userId(board.getUser().getId())
                        .userName(board.getUser().getUserName())
                        .rowNum(count--)
                        .build();
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    public BoardDetailResponseDTO getBoard(String category, int id) {

//
//
//            log.info("Wefwfewfe", String.valueOf(board.getBoardId()));
//
//            BoardDetailResponseDTO dto = BoardDetailResponseDTO.builder()
//                    .id(board.getBoardId())
//                    .title(board.getTitle())
//                    .content(board.getContent())
//                    .category(board.getCategory())
//                    .regDate(board.getRegDate())
//                    .userId(board.getUser().getId())
//                    .build();



        if("entire".equals(category)){
            Board board = boardRepository.findByBoardId(id);



            BoardDetailResponseDTO build =
                    BoardDetailResponseDTO.builder()
                    .id(board.getBoardId())
                    .title(board.getTitle())
                    .category(category)
                            .content(board.getContent())
                    .regDate(board.getRegDate())
                    .userId(board.getUser().getId())
                    .userName(board.getUser().getUserName())
                    .build();

            return build;

        }else {
            Board board = boardRepository.findByCategoryAndId(category, id);



            BoardDetailResponseDTO build =
                    BoardDetailResponseDTO.builder()
                    .id(board.getBoardId())
                    .title(board.getTitle())
                    .category(category)
                            .content(board.getContent())
                    .regDate(board.getRegDate())
                    .userId(board.getUser().getId())
                    .userName(board.getUser().getUserName())
                    .build();

            return build;
        }



    }

    public BoardDetailResponseDTO registerBoard(
            final BoardRequestDTO requestDTO,
            final TokenUserInfo userInfo
            ) throws RuntimeException {
        // 게시글 등록은 유저만 할 수 있으므로 유저 엔티티 조회해 전달
        User user = Objects.requireNonNull(getUser(userInfo.getUserId()));
        log.info("글을 등록한 유저: {}", user);

        // 등록
        Board saved = boardRepository.save(requestDTO.toEntity(user));

        log.info("할 일 저장 완료! saved: {}", saved);
        return getBoard(saved.getCategory(), saved.getBoardId());
//        return getBoard(requestDTO.toEntity(user).getCategory(), requestDTO.toEntity(user).getBoardId());
    }

    // 유저 조회 메서드
    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        return user;
    }


    public BoardDetailResponseDTO updateBoard(final BoardRequestDTO requestDTO,
                                              final String userId,
                                              int boardId) throws RuntimeException{
        log.info("게시글 수정 서비스 작동!");
        Board entityToBeApplied = requestDTO.toEntity(getUser(userId)); // 엔티티로 변환

        Board targetBoard = boardRepository.findById(boardId).orElseThrow(null);

        targetBoard.setCategory(entityToBeApplied.getCategory());
        targetBoard.setTitle(entityToBeApplied.getTitle());
        targetBoard.setContent(entityToBeApplied.getContent());

        return getBoard(targetBoard.getCategory(), boardId); // dto로 변환 후 리턴


//        targetBoard.ifPresent(boardRepository::save); // null 아닌 경우에만 실행.

        // 수정 대상인 보드
//        User user = getUser(userId);
//        List<Board> boardList = boardRepository.findAllByUser(user);

//        return getBoard(requestDTO.getCategory(), requestDTO.getBoardId());


    }



    public BoardResponseDTO deleteBoard(int id, String userId) {
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("대상이 존재하지 않아 삭제 실패!")
            );
            if(!userId.equals(board.getUser().getId())){
                log.warn("작성자가 아니므로 삭제 불가");
                throw new RuntimeException("작성 권한이 없습니다.");
            }



        Query query = entityManager.createQuery("DELETE FROM Comment c WHERE c.board.boardId = :boardId");
        query.setParameter("boardId", id);
        query.executeUpdate();


        boardRepository.deleteById(id);
            return null;
    }


}


