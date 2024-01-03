package com.example.demo.shareapi.api;

import com.example.demo.auth.TokenUserInfo;
import com.example.demo.shareapi.dto.request.ApprovalDateDTO;

import com.example.demo.shareapi.dto.request.ShareCommentRequestDTO;
import com.example.demo.shareapi.dto.request.ShareRequestDTO;
import com.example.demo.shareapi.dto.request.ShareUpdateRequestDTO;
import com.example.demo.shareapi.dto.response.ShareCommentResponseDTO;
import com.example.demo.shareapi.dto.response.ShareDetailResponseDTO;
import com.example.demo.shareapi.dto.response.ShareResponseDTO;
import com.example.demo.shareapi.dto.response.ShareSetApprovalResponseDTO;
import com.example.demo.shareapi.entity.ApprovalStatus;
import com.example.demo.shareapi.service.ShareCommentService;
import com.example.demo.shareapi.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/donation")
@CrossOrigin
public class ShareController {

    private final ShareService shareService;
    private final ShareCommentService shareCommentService;



    // 등록된 게시글 목록 조회 요청 처리
    @GetMapping
    public ResponseEntity<?> getBoardList(){
        log.info("/donation : GET - share board List Request");
        List<ShareResponseDTO> boardList = shareService.getBoardList();


        return ResponseEntity.ok().body(boardList);
    }

    /************************* 관리자의 요청 처리 ******************************/
    // 관리자의 승인할 게시물 목록 조회 요청 처리
    @GetMapping("/approval")
//    @AuthenticationPrincipal(role = Role.ADMIN)
    public ResponseEntity<?> getAdminBoardList(@AuthenticationPrincipal TokenUserInfo userInfo){
        log.info("/donation/approval : GET - ADMIN의 share board List Request");
        List<ShareResponseDTO> boardList = shareService.getNotYetApprovedBoardList();

        return ResponseEntity.ok().body(boardList);
    }

    // 승인할 게시글 상세보기 요청 처리
    @GetMapping("/approval/{board_id}")
    public ResponseEntity<?> getNotYetApproveBoard(@AuthenticationPrincipal TokenUserInfo userInfo,
                                                   @PathVariable("board_id") int id){
        log.info("/donation/approval/{} GET - ADMIN의 share board Detail Request", id);
        ShareDetailResponseDTO responseDTO = shareService.getBoardOfAdmin(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    // 승인 요청 처리 : 승인된 날짜 넣고 해당 글 리턴
    @PostMapping("/approval/complete/{share_id}/{flag}")
    public  ResponseEntity<?> approve(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable("share_id") int shareId,
            @PathVariable("flag")String approvalStatus
            ){
        log.info("/donation/approval/complete : POST - ADMIN의 승인 Request");
        log.info("ENUM 값 수신 완료: {}", approvalStatus);

        ShareSetApprovalResponseDTO board
                = shareService.setApprovalDate(shareId,
                ApprovalStatus.valueOf(ApprovalStatus.class, approvalStatus.toUpperCase()));

        return ResponseEntity.ok().body(board);
    }
    /************************** 관리자의 요청 처리 끝 ***********************************/

    // 게시글 상세보기 요청 처리
    @GetMapping("/{board_id}")
    public ResponseEntity<?> getBoard(@PathVariable("board_id") int id){
        log.info("/donation/{} GET - share board Detail Request", id);
        ShareDetailResponseDTO responseDTO = shareService.getBoard(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    // 게시글 등록 요청 처리 @ requestfile multipartfile->list string : service
    @PostMapping(value = "/regist", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> registerBoard(
                                @AuthenticationPrincipal TokenUserInfo userInfo,
                                @RequestPart(value = "uploadImages", required = false) List<MultipartFile> files,
                                @Validated @RequestPart("requestDTO") ShareRequestDTO requestDTO,
                                BindingResult result
                                           ){

        log.info("/donation : POST - board Register Request");
        log.info("RequestDTO: {} ", requestDTO);
        log.info("TokenUserInfo: {} ", userInfo);
        log.info("files: {} ", files);
        /////

        /////
        if(result.hasErrors()) {
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());
            return ResponseEntity
                    .badRequest() // 400
                    .body(result.getFieldError());
        }

        try {
            int id = shareService.registerBoard(files, requestDTO, userInfo);

            return ResponseEntity.status(HttpStatus.OK).body(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                    .body(e.getMessage());
        }


    }


    // 게시글 수정
    @RequestMapping(
            method = {RequestMethod.PUT, RequestMethod.PATCH},
            value = "/{id}",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<?> updateBoard(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestPart(value = "uploadImages", required = false) List<MultipartFile> files,
            @Validated @RequestPart ShareUpdateRequestDTO requestDTO,
            @PathVariable int id,
            BindingResult result,
            HttpServletRequest request // 요청 방식 구분



    ){
        log.info("/donation/{} : {} - share board Update Request",id, request.getMethod());
        log.info("modifying dto: {}, id: {}", requestDTO, id);

        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            ShareDetailResponseDTO responseDTO
                    = shareService.updateBoard(requestDTO, userInfo.getUserId(), id, files);
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }


    }


    // 게시글 삭제
    @DeleteMapping("/{board_id}")
    public ResponseEntity<?> deleteBoard(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable("board_id") int id){
        log.info("/donation/{} : DELETE  - id가 {}인 board Delete Request",
                 id, id);

        try {
            ShareDetailResponseDTO responseDTO = shareService.deleteBoard(id, userInfo);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }


    }

    /************************** 댓글 요청 처리 ***************************/
    // 댓글 목록 조회 요청 처리
    @GetMapping("/{b_id}/reply")
    public ResponseEntity<?> getCommentList(@PathVariable("b_id") int boardId){
        log.info("/donation/{}/reply : GET - id가 {}인 board에 달린 댓글 List Request",
        boardId, boardId);
        List<ShareCommentResponseDTO> commentList = shareCommentService.getCommentList(boardId);

        return ResponseEntity.ok().body(commentList);
    }

    // 댓글 추가
    @PostMapping("/{b_id}/reply")
    public ResponseEntity<?> registerComment(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable("b_id") int boardId,
            @Validated @RequestBody ShareCommentRequestDTO requestDTO,
            BindingResult result
    ){
        log.info("/donation/{}/reply-board POST - comment Register Request", boardId);
        log.info("RequestDTO: {} ", requestDTO);
        log.info("TokenUserInfo: {} ", userInfo);

        if(result.hasErrors()) {
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());
            return ResponseEntity
                    .badRequest() // 400
                    .body(result.getFieldError());
        }

        try {
            ShareCommentResponseDTO responseDTO
                    = shareCommentService.registerComment(boardId, requestDTO, userInfo);

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                    .body(e.getMessage());
        }


    }

    // 댓글 수정
    @PutMapping("/{b_id}/reply/{r_id}")
    public ResponseEntity<?> updateComment(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody ShareCommentRequestDTO requestDTO,
            @PathVariable("b_id") int boardId,
            @PathVariable("r_id") int commentId,
            BindingResult result
    ){
        log.info("/donation/{}/reply/{}  - comment Update Request",
             boardId, commentId);
        log.info("modifying dto: {}, id: {}", requestDTO, commentId);

        if(result.hasErrors()){
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            ShareCommentResponseDTO responseDTO = shareCommentService.updateComment(requestDTO, userInfo.getUserId(), boardId, commentId);
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }

    }


    // 댓글 삭제
    @DeleteMapping("/{b_id}/reply/{r_id}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal TokenUserInfo userInfo,
                                           @PathVariable("b_id") int boardId,
                                           @PathVariable("r_id") int commentId){
        log.info("/donation/{}/reply/{} : DELETE  - comment Delete Request", boardId, commentId);

        try {
            ShareCommentResponseDTO responseDTO
                    = shareCommentService.deleteBoard(userInfo.getUserId(), commentId);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /************************** 마이페이지 요청 *****************************/
    // 사용자의 마이페이지에서 미승인된 게시물 목록 조회 요청 처리
    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPageShareList(@AuthenticationPrincipal TokenUserInfo userInfo){
        log.info("/donation/mypage : GET - My share board List Request");
        List<ShareResponseDTO> boardList = shareService.getMyNotYetApprovedBoardList(userInfo);

        return ResponseEntity.ok().body(boardList);
    }

    // 미승인된 게시물 상세보기 요청 처리

}
