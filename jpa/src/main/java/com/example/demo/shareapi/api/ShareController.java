package com.example.demo.shareapi.api;

import com.example.demo.auth.TokenUserInfo;
import com.example.demo.shareapi.dto.*;
import com.example.demo.shareapi.service.ShareCommentService;
import com.example.demo.shareapi.service.ShareService;
import com.example.demo.userapi.entity.Role;
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

import javax.management.relation.RoleList;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
        List<ShareResponseDTO> boardList = shareService.getBoardList();

        return ResponseEntity.ok().body(boardList);
    }

    // 승인할 게시글 상세보기 요청 처리

    // 승인 요청 처리 : 승인된 날짜 받아서 넣고 해당 글 리턴
    @PostMapping("/approval/complete/{share_id}")
    public  ResponseEntity<?> approve(@PathVariable("share_id") int shareId, ApproveDateDTO dto){
        log.info("/donation/approval/complete : POST - ADMIN의 승인 Request");
        ShareResponseDTO board = shareService.setApprovalDate(shareId, dto);

        return ResponseEntity.ok().body(board);
    }


    /**************************************************************************/

    // 게시글 상세보기 요청 처리
    @GetMapping("/{category}/{id}")
    public ResponseEntity<?> getBoard(@PathVariable String category,
                                      @PathVariable int id){
        log.info("/api/onelife-board/{}/{} GET - board Detail Request", category, id);
        ShareDetailResponseDTO responseDTO = shareService.getBoard(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    // 게시글 등록 요청 처리 @ requestfile multipartfile->list string : service
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> registerBoard(
                                @AuthenticationPrincipal TokenUserInfo userInfo,
                                @RequestPart(value = "uploadImages", required = false) List<MultipartFile> files,
                                @Validated @RequestPart ShareRequestDTO requestDTO,
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
//            String uploadedFilePath = null;
//            if(files != null){
//                files.toString()
//                // 전달받은 프로필 이미지를 먼저 지정된 경로에 저장한 후 DB 저장을 위해 경로를 받아오자.
//                uploadedFilePath = Service.uploadProfileImage(files);
//
//            }

//            ShareDetailResponseDTO responseDTO = shareService.registerBoard(files, requestDTO, userInfo);
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
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, value = "/{category}/{id}")
//    @PutMapping("/{category}/{id}")
    public ResponseEntity<?> updateBoard(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody ShareRequestDTO requestDTO,
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
            ShareDetailResponseDTO responseDTO
                    = shareService.updateBoard(requestDTO, userInfo.getUserId(), id);
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
            ShareResponseDTO responseDTO = shareService.deleteBoard(id, userInfo.getUserId());
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
    @PutMapping("/{category}/{b_id}/reply/{r_id}")
    public ResponseEntity<?> updateComment(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody ShareCommentRequestDTO requestDTO,
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
            ShareCommentResponseDTO responseDTO = shareCommentService.updateComment(requestDTO, userInfo.getUserId(), boardId, commentId);
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
            ShareCommentResponseDTO responseDTO
                    = shareCommentService.deleteBoard(userInfo.getUserId(), commentId);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
