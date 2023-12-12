package com.example.demo.boardapi.service;

import com.example.demo.boardapi.dto.BoardDetailResponseDTO;
import com.example.demo.boardapi.dto.BoardRequestDTO;
import com.example.demo.boardapi.dto.BoardResponseDTO;
import com.example.demo.boardapi.entity.Board;
import com.example.demo.boardapi.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
//    private final UserRepository userRepository;

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
                                        .userId(board.getUserId())
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
                .userId(board.getUserId())
                .build();

        return dto;
    }

    public void registerBoard(String category, // 글쓴이 정보도 요청와야?
                              @Validated @RequestBody BoardRequestDTO requestDTO,
                              BindingResult result) {
        boardRepository.save()
    }
}
