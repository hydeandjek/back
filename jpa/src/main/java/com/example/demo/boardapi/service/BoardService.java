package com.example.demo.boardapi.service;

import com.example.demo.boardapi.dto.BoardDetailResponseDTO;
import com.example.demo.boardapi.dto.BoardRequestDTO;
import com.example.demo.boardapi.dto.BoardResponseDTO;
import com.example.demo.boardapi.entity.Board;
import com.example.demo.boardapi.repository.BoardRepository;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.auth.TokenUserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<BoardResponseDTO> getBoardList(String category) {
//        List<Board> list = BoardRepository.findAll(Sort.by(Sort.Direction.DESC, "boardid"));
        List<Board> boardList = boardRepository.findAllByCategory(category);

        List<BoardResponseDTO> dtoList = new ArrayList<>();
        for(Board board : boardList){
            BoardResponseDTO dto = BoardResponseDTO.builder()
                                        .id(board.getBoardId())
                                        .title(board.getTitle())
                                        .category(category)
                                        .regDate(board.getRegDate())
                                        .userId(board.getUser().getId())
                                        .build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    public BoardDetailResponseDTO getBoard(String category, int id) {
        Board board = boardRepository.findByCategoryAndId(category, id);

        BoardDetailResponseDTO dto = BoardDetailResponseDTO.builder()
                .id(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .regDate(board.getRegDate())
                .userId(board.getUser().getId())
                .build();

        return dto;
    }

    public void registerBoard(
            final BoardRequestDTO requestDTO,
            final TokenUserInfo userInfo
            ) throws RuntimeException {
        // 게시글 등록은 유저만 할 수 있으므로 유저 엔티티 조회해 전달
        User user = Objects.requireNonNull(getUser(userInfo.getUserId()));

        // 등록
        boardRepository.save(requestDTO.toEntity(user));
        log.info("할 일 저장 완료! 제목: {}", requestDTO.getTitle());

    }

    // 유저 조회 메서드
    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        return user;
    }


}


