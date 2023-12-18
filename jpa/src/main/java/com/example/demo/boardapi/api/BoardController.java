package com.example.demo.boardapi.api;

import com.example.demo.auth.TokenUserInfo;
import com.example.demo.boardapi.dto.*;
import com.example.demo.boardapi.service.BoardService;
import com.example.demo.boardapi.service.CommentService;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/onelife-board")
@CrossOrigin
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;


    // 카테고리별 게시글 목록 요청 처리
    @GetMapping("/{category}")
    public ResponseEntity<?> getBoardList(@PathVariable String category){
        log.info("/api/onelife-board/{} GET - board List Request", category);
        List<BoardResponseDTO> boardList = boardService.getBoardList(category);


        return ResponseEntity.ok().body(boardList);
    }

    // 게시글 상세보기 요청 처리
    @GetMapping("/{category}/{id}")
    public ResponseEntity<?> getBoard(@PathVariable String category,
                                      @PathVariable int id){
        log.info("/api/onelife-board/{}/{} GET - board Detail Request", category, id);
        BoardDetailResponseDTO responseDTO = boardService.getBoard(category, id);
        return ResponseEntity.ok().body(responseDTO);
    }

    // 게시글 추가
    @PostMapping("/{category}")
    public ResponseEntity<?> registerBoard(
                                @AuthenticationPrincipal TokenUserInfo userInfo,
                                @Validated @RequestBody BoardRequestDTO requestDTO,
                                BindingResult result
                                           ){

        log.info("/api/onelife-board/{} POST - board Register Request", requestDTO.getCategory());
        log.info("RequestDTO: {} ", requestDTO);
        log.info("TokenUserInfo: {} ", userInfo);

        if(result.hasErrors()) {
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());
            return ResponseEntity
                    .badRequest() // 400
                    .body(result.getFieldError());
        }

        try {
            BoardDetailResponseDTO responseDTO = boardService.registerBoard(requestDTO, userInfo);

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                    .body(e.getMessage());
        }


    }


    // 게시글 수정
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, value = "/{category}/{id}")
//    @PutMapping("/{category}/{id}")
    public ResponseEntity<?> updateBoard(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody BoardRequestDTO requestDTO,
            @PathVariable int id,
            BindingResult result,
            HttpServletRequest request // 요청 방식 구분
    ){
        log.info("/api/onelife-board {}  - board Update Request", request.getMethod());
        log.info("modifying dto: {}, id: {}", requestDTO, id);

        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            BoardDetailResponseDTO responseDTO
                    = boardService.updateBoard(requestDTO, userInfo.getUserId(), id);
            return ResponseEntity.ok().body(responseDTO);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }


    }


    // 게시글 삭제
    @DeleteMapping("/{category}/{id}")
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal TokenUserInfo userInfo,
                              @PathVariable int id,
                              @PathVariable String category){
        log.info("/api/onelife-board/{}/{} DELETE  - id가 {}인 board Delete Request",
                category, id, id);

        try {
            BoardResponseDTO responseDTO = boardService.deleteBoard(id, userInfo.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }


    }

    /************************** 댓글 요청 처리 ***************************/
    // 댓글 목록 조회 요청 처리
    @GetMapping("/{category}/{b_id}/reply")
    public ResponseEntity<?> getCommentList(@PathVariable String category, @PathVariable("b_id") int boardId){
        log.info("/api/onelife-board/{}/{}/reply GET - id가 {}인 board에 달린 댓글 List Request"
        ,category, boardId, boardId);
        List<CommentResponseDTO> commentList = commentService.getCommentList(boardId);

        return ResponseEntity.ok().body(commentList);
    }

    // 댓글 추가
    @PostMapping("/{b_id}/reply")
    public ResponseEntity<?> registerComment(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable("b_id") int boardId,
            @Validated @RequestBody CommentRequestDTO requestDTO,
            BindingResult result
    ){
        log.info("/api/onelife-board POST - comment Register Request");
        log.info("RequestDTO: {} ", requestDTO);
        log.info("TokenUserInfo: {} ", userInfo);

        if(result.hasErrors()) {
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());
            return ResponseEntity
                    .badRequest() // 400
                    .body(result.getFieldError());
        }

        try {
            CommentResponseDTO responseDTO
                    = commentService.registerComment(boardId, requestDTO, userInfo);

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                    .body(e.getMessage());
        }


    }

    // 댓글 수정
    @PutMapping("/{category}/{b_id}/reply/{r_id}")
    public ResponseEntity<?> updateComment(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody CommentRequestDTO requestDTO,
            @PathVariable String category,
            @PathVariable("b_id") int boardId,
            @PathVariable("r_id") int commentId,
            BindingResult result
    ){
        log.info("/api/onelife-board/{}/{}/reply/{}  - comment Update Request",
                category, boardId, commentId);
        log.info("modifying dto: {}, id: {}", requestDTO, commentId);

        if(result.hasErrors()){
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            CommentResponseDTO responseDTO = commentService.updateComment(requestDTO, userInfo.getUserId(), boardId, commentId);
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }

    }


    // 댓글 삭제
    @DeleteMapping("/{category}/{b_id}/reply/{r_id}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal TokenUserInfo userInfo,
                                           @PathVariable String category,
                                           @PathVariable("b_id") int boardId,
                                           @PathVariable("r_id") int commentId){
        log.info("/api/onelife-board/{}/{}/reply/{} DELETE  - comment Delete Request",
                category, boardId, commentId);

        try {
            CommentResponseDTO responseDTO
                    = commentService.deleteBoard(userInfo.getUserId(), commentId);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
