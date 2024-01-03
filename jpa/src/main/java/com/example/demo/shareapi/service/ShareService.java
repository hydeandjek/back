package com.example.demo.shareapi.service;

import ch.qos.logback.core.util.FileUtil;
import com.example.demo.auth.TokenUserInfo;
import com.example.demo.boardapi.repository.BoardRepository;
import com.example.demo.shareapi.config.AwsConfig;
import com.example.demo.shareapi.dto.request.ApprovalDateDTO;

import com.example.demo.shareapi.dto.request.ShareUpdateRequestDTO;
import com.example.demo.shareapi.dto.response.ShareCommentResponseDTO;
import com.example.demo.shareapi.dto.response.ShareDetailResponseDTO;
import com.example.demo.shareapi.dto.request.ShareRequestDTO;
import com.example.demo.shareapi.dto.response.ShareResponseDTO;
import com.example.demo.shareapi.dto.response.ShareSetApprovalResponseDTO;
import com.example.demo.shareapi.entity.ApprovalStatus;
import com.example.demo.shareapi.entity.Images;
import com.example.demo.shareapi.entity.Share;
import com.example.demo.shareapi.entity.ShareComment;
import com.example.demo.shareapi.repository.ImageRepository;
import com.example.demo.shareapi.repository.ShareCommentRepository;
import com.example.demo.shareapi.repository.ShareRepository;
import com.example.demo.userapi.entity.Role;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.ResponseInputStream;

import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ShareService {

    private final ShareRepository shareRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ShareCommentRepository shareCommentRepository;

    private final S3Service s3Service;

    public List<ShareResponseDTO> getBoardList() {
//        List<Board> list = BoardRepository.findAll(Sort.by(Sort.Direction.DESC, "boardid"));
        List<Share> boardList = shareRepository.findApprovedShares();

        List<ShareResponseDTO> dtoList = new ArrayList<>();
        for(Share board : boardList){
            List<Images> imagesList = imageRepository.findAllByBoardId(board.getShareId());
            String filePath = imagesList.get(0).getFilePath(); //게시글id에 따른 첫번째 이미지의 경로

            int countedComment = shareCommentRepository.countByBoard(board.getShareId());

            ShareResponseDTO dto = ShareResponseDTO.builder()
                                        .id(board.getShareId())
                                        .title(board.getTitle())
                                        .regDate(board.getRegDate())
                                        .userId(board.getUser().getId())
                                        .imageUrl(filePath)
                                        .commentCount(countedComment)
                                        .content(board.getContent())
                                        .approvalDate(board.getApprovalDate())
                                        .userName(userRepository.findById(board.getUser().getId()).map(User::getUserName).orElse(null))
                                        .build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<ShareResponseDTO> getNotYetApprovedBoardList() {
//        List<Board> list = BoardRepository.findAll(Sort.by(Sort.Direction.DESC, "boardid"));
//        List<Share> boardList = shareRepository.findAll();
        List<Share> notApprovedShares = shareRepository.findHoldShares();

        List<ShareResponseDTO> dtoList = new ArrayList<>();
        for(Share board : notApprovedShares){
            List<Images> imagesList = imageRepository.findAllByBoardId(board.getShareId());
            String filePath = null;
            if(!imagesList.isEmpty()){
                filePath = imagesList.get(0).getFilePath(); //게시글id에 따른 첫번째 이미지의 경로
            }

            int countedComment = shareCommentRepository.countByBoard(board.getShareId());

            ShareResponseDTO dto = ShareResponseDTO.builder()
                    .id(board.getShareId())
                    .title(board.getTitle())
                    .regDate(board.getRegDate())
                    .approvalDate(null)
                    .userId(board.getUser().getId())
                    .imageUrl(filePath)
                    .commentCount(countedComment)
                    .content(board.getContent())
                    .userName(userRepository.findById(board.getUser().getId()).map(User::getUserName).orElse(null))
                    .build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<ShareResponseDTO> getMyNotYetApprovedBoardList(TokenUserInfo userInfo) {
//        List<Board> list = BoardRepository.findAll(Sort.by(Sort.Direction.DESC, "boardid"));
//        List<Share> boardList = shareRepository.findAll();
        int countByUserApproved
                = shareRepository.countByUserApproved(getUser(userInfo.getUserId()));
        int countByUserYetApproved
                = shareRepository.countByUserYetApproved(getUser(userInfo.getUserId()));

        if(countByUserYetApproved > 0){
            List<Share> allByUserYetApprovedShares
                    = shareRepository.findAllByUserYetApprovedShares(getUser(userInfo.getUserId()));
            List<ShareResponseDTO> dtoList = new ArrayList<>();
            for(Share board : allByUserYetApprovedShares){
                List<Images> imagesList = imageRepository.findAllByBoardId(board.getShareId());
                String filePath = imagesList.get(0).getFilePath(); //게시글id에 따른 첫번째 이미지의 경로

                int countedComment = shareCommentRepository.countByBoard(board.getShareId());

                ShareResponseDTO dto = ShareResponseDTO.builder()
                        .id(board.getShareId())
                        .title(board.getTitle())
                        .regDate(board.getRegDate())
                        .approvalDate(null)
                        .userId(board.getUser().getId())
                        .imageUrl(filePath)
                        .commentCount(countedComment)
                        .build();
                dtoList.add(dto);
            }
            return dtoList;
        } else {
            List<ShareResponseDTO> dtoList = new ArrayList<>();
//            List<Images> imagesList = imageRepository.findAllByBoardId(board.getShareId());
//            String filePath = imagesList.get(0).getFilePath(); //게시글id에 따른 첫번째 이미지의 경로
//
//            int countedComment = shareCommentRepository.countByBoard(board.getShareId());

            ShareResponseDTO dto = ShareResponseDTO.builder()
//                    .id(board.getShareId())
                    .title("존재하지 않습니다.")
//                    .regDate(board.getRegDate())
//                    .approvalDate(null)
//                    .approvalFlag(null)
                    .userId(userInfo.getUserId())
//                    .imageUrl(filePath)
//                    .commentCount(countedComment)
                    .build();
            dtoList.add(dto);
            return dtoList;

        }




    }

    public ShareSetApprovalResponseDTO setApprovalDate(int shareId, ApprovalStatus approvalStatus) {
        Share share = shareRepository.findById(shareId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 게시글이 없습니다.")
        );
        log.info("승인 여부 확인 : {}, 승인일: {}", share.getApprovalFlag(), share.getApprovalDate());
        if (approvalStatus == ApprovalStatus.APPROVE){
            share.setApprovalFlag(ApprovalStatus.APPROVE);
        } else if(approvalStatus == ApprovalStatus.REJECT) {
            share.setApprovalFlag(ApprovalStatus.REJECT);
        }
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = currentDate.format(formatter);
        share.setApprovalDate(formattedDate);
        log.info("{} 완료!", share.getApprovalFlag());

        List<Images> imagesList = imageRepository.findAllByBoardId(shareId);
        List<String> imgUrlList = new ArrayList<>();
        for(Images images:imagesList){
            imgUrlList.add(images.getFilePath());
        }

        List<ShareComment> shareCommentList = shareCommentRepository.findAllByBoardId(shareId);
        List<ShareCommentResponseDTO> shareCommentResponseDTOList = new ArrayList<>();
        for(ShareComment shareComment:shareCommentList){
            ShareCommentResponseDTO shareCommentResponseDTO = ShareCommentResponseDTO.builder()
                    .content(shareComment.getContent())
                    .regDate(shareComment.getRegDate())
                    .userId(shareComment.getUser().getId())
                    .boardId(shareComment.getShare().getShareId())
                    .build();
            shareCommentResponseDTOList.add(shareCommentResponseDTO);
        }

        return ShareSetApprovalResponseDTO.builder()
                .id(share.getShareId())
                .title(share.getTitle())
                .regDate(share.getRegDate())
                .userId(share.getUser().getId())
                .approvalDate(share.getApprovalDate())
                .approvalFlag(share.getApprovalFlag())
                .imageUrl(imgUrlList)
                .commentCount(shareCommentResponseDTOList.size()) // 댓글 수
                .build();
    }

    public ShareDetailResponseDTO getBoard(int id) {
        Share share = shareRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 id의 나눔 게시글은 없습니다.")
        );
        // 게시글의 이미지들 리스트
        List<Images> imagesList = imageRepository.findAllByBoardId(id);
//        for (Images images : imagesList){
//            Images img = Images
//                    .builder()
//                    .filePath(images.getFilePath())
//                    .build();
//
//        } // 이들의 경로 가진 Images 객체 생성.
        List<ShareComment> shareCommentList = shareCommentRepository.findAllByBoardId(id);

        List<ShareCommentResponseDTO> shareCommentResponseDTOList = new ArrayList<>();
        for(ShareComment shareComment:shareCommentList){
            ShareCommentResponseDTO shareCommentResponseDTO = ShareCommentResponseDTO.builder()
                    .commentId(shareComment.getShareCommentId())
                    .content(shareComment.getContent())
                    .regDate(shareComment.getRegDate())
                    .userId(shareComment.getUser().getId())
                    .boardId(shareComment.getShare().getShareId())
                    .userName(userRepository.findById(shareComment.getUser().getId()).map(User::getUserName).orElse(null))
                    .build();
            shareCommentResponseDTOList.add(shareCommentResponseDTO);
        }


        ShareDetailResponseDTO dto = ShareDetailResponseDTO.builder()
                .id(share.getShareId())
                .title(share.getTitle())
                .content(share.getContent())
                .uploadImages(
                        imagesList // 각 이미지들은 filePath 가짐

                )
                // Images 엔티티의 파일저장경로(스트링) 리스트 괄호안에 넣기
                .regDate(share.getRegDate())
                .approvalDate(share.getApprovalDate())
                .comments(shareCommentResponseDTOList) // 코멘트 리스트
                .userId(share.getUser().getId())
                .userName(userRepository.findById(share.getUser().getId()).map(User::getUserName).orElse(null))
                .approvalFlag(share.getApprovalFlag())
                .build();

        return dto;
    }

    public ShareDetailResponseDTO getBoardOfAdmin(int id) {
        Share share = shareRepository.findByIdYetApprovedShares(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 id의 나눔 게시글은 없습니다.")
        );
        // 게시글의 이미지들 리스트
        List<Images> imagesList = imageRepository.findAllByBoardId(id);
//        for (Images images : imagesList){
//            Images img = Images
//                    .builder()
//                    .filePath(images.getFilePath())
//                    .build();
//
//        } // 이들의 경로 가진 Images 객체 생성.
        List<ShareComment> shareCommentList = shareCommentRepository.findAllByBoardId(id);

        List<ShareCommentResponseDTO> shareCommentResponseDTOList = new ArrayList<>();
        for(ShareComment shareComment:shareCommentList){
            ShareCommentResponseDTO shareCommentResponseDTO = ShareCommentResponseDTO.builder()
                    .commentId(shareComment.getShareCommentId())
                    .content(shareComment.getContent())
                    .regDate(shareComment.getRegDate())
                    .userId(shareComment.getUser().getId())
                    .boardId(shareComment.getShare().getShareId())
                    .userName(userRepository.findById(share.getUser().getId()).map(User::getUserName).orElse(null))
                    .build();
            shareCommentResponseDTOList.add(shareCommentResponseDTO);
        }


        ShareDetailResponseDTO dto = ShareDetailResponseDTO.builder()
                .id(share.getShareId())
                .title(share.getTitle())
                .content(share.getContent())
                .uploadImages(
                        imagesList // 각 이미지들은 filePath 가짐

                )
                // Images 엔티티의 파일저장경로(스트링) 리스트 괄호안에 넣기
                .regDate(share.getRegDate())
                .approvalDate(share.getApprovalDate())
                .comments(shareCommentResponseDTOList) // 코멘트 리스트
                .userId(share.getUser().getId())
                .userName(userRepository.findById(share.getUser().getId()).map(User::getUserName).orElse(null))
                .approvalFlag(share.getApprovalFlag())
                .build();

        return dto;
    }

    /* 1. 파일 업로드 */
/*    public String upload(MultipartFile multipartFile, String s3FileName) throws IOException {
        // 메타데이터 생성
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());
        // putObject(버킷명, 파일명, 파일데이터, 메타데이터)로 S3에 객체 등록
        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
        // 등록된 객체의 url 반환 (decoder: url 안의 한글or특수문자 깨짐 방지)
        return URLDecoder.decode(amazonS3.getUrl(bucket, s3FileName).toString(), "utf-8");
    }
    public void uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .build();
            S3Client.putObject(request, file.getInputStream());
            System.out.println("File uploaded successfully to AWS S3.");
        } catch (Exception e) {
            System.out.println("Error uploading file to AWS S3: " + e.getMessage());
        }
    }*/

    public int registerBoard(
            final List<MultipartFile> files,
            final ShareRequestDTO requestDTO,
            final TokenUserInfo userInfo
            ) throws RuntimeException, IOException {
        // 게시글 등록은 유저만 할 수 있으므로 유저 엔티티 조회해 전달
        User user = Objects.requireNonNull(getUser(userInfo.getUserId()));

        log.info("글을 등록한 유저: {}", user);
        log.info("ShareRequestDTO: {}", requestDTO);
        log.info("files: {}", files);

        Share share = Share.builder()
                .title(requestDTO.getTitle())
//                .uploadImages(imagesArrayList)
                .content(requestDTO.getContent())
                .user(user)
//                .comments(shareCommentList)
//                .approvalFlag(ApprovalStatus.HOLD)
                .build();
        int id = shareRepository.save(share).getShareId();

        List<String> fileNameList = new ArrayList<>();

        if(!Objects.isNull(files)){
            for (MultipartFile file : files){

                String originFileName = file.getOriginalFilename();
                log.info("original file name: {}", originFileName);

                long size = file.getSize();
                log.info("size = {}", size);

                String contentType = file.getContentType();
                log.info("content type={}", contentType);

                // 서버에 저장되는 경로 (나중에 -> AWS S3)
                //String filePath = "C:/test/final_project_file_upload/"+originFileName;

                // 서버에 저장되는 이름 (파일명이 중복될 수 있으므로 서버에는 고유값으로 파일명 저장)
                UUID uuid = UUID.randomUUID();
                String serverFileName = uuid + originFileName;

                String url = s3Service.uploadToS3Bucket(file.getBytes(), serverFileName);

                Images images = new Images(originFileName, serverFileName, url, size);
//                imagesArrayList.add(images);
//                share.setUploadImages(imagesArrayList);

                images.setShare(share); // 이미지를 게시글과 연결
                imageRepository.save(images); // 이미지 테이블에 저장
//
                //file.transferTo(new File(filePath)); // 로컬에 이미지 저장
            }
        }



        return id;
//
//        log.info("할 일 저장 완료! saved: {}", saved);
//        return getBoard(saved.getShareId());
//        return getBoard(requestDTO.toEntity(user).getCategory(), requestDTO.toEntity(user).getBoardId());
    }

    // blob to byte
    private byte[] blobToBytes(Blob blob){
        BufferedInputStream is = null;
        byte[] bytes = null;
        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;

            while (offset < len &&
                    (read = is.read(bytes, offset, len-offset)) >= 0){
                offset += read;
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException("blob을 읽지 못하였습니다.");
        }
        return bytes;
    }

    // 유저 조회 메서드
    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        return user;
    }


    public ShareDetailResponseDTO updateBoard(final ShareUpdateRequestDTO requestDTO,
                                              final String userId,
                                              int boardId,
                                              List<MultipartFile> files) throws RuntimeException, IOException {
        log.info("게시글 수정 서비스 작동!");

        // 수정 대상
        Share targetBoard = shareRepository.findById(boardId).orElseThrow(null);

        if(!userId.equals(targetBoard.getUser().getId())){
           throw new RuntimeException("수정 권한이 없습니다.");
       }

        // 수정 반영할 것 엔티티로 변환
        Share entityToBeApplied = requestDTO.toEntity(getUser(userId)); // 엔티티로 변환


        if(!Objects.isNull(files)){ //유저가 이미지도 수정 요청했다면

            imageRepository.deleteByBoardId(boardId); // 해당 게시글의 이미지들 삭제

            for (MultipartFile file : files){
                String originFileName = file.getOriginalFilename();
                log.info("original file name: {}", originFileName);

                long size = file.getSize();
                log.info("size = {}", size);

                String contentType = file.getContentType();
                log.info("content type={}", contentType);

                // 서버에 저장되는 경로 (나중에 -> AWS S3)
                String filePath = "C:/test/final_project_file_upload/"+originFileName;

                // 서버에 저장되는 이름 (파일명이 중복될 수 있으므로 서버에는 고유값으로 파일명 저장)
                UUID uuid = UUID.randomUUID();
                String serverFileName = uuid + originFileName;

                Images images = new Images(originFileName, serverFileName, filePath, size);
//                imagesArrayList.add(images);
//                share.setUploadImages(imagesArrayList);

                images.setShare(targetBoard); // 이미지를 게시글과 연결
                imageRepository.save(images); // 이미지 테이블에 저장
//
                file.transferTo(new File(filePath)); // 로컬에 이미지 저장
            }
        }

        targetBoard.setTitle(entityToBeApplied.getTitle());
        targetBoard.setContent(entityToBeApplied.getContent());

        return getBoard(boardId); // dto로 변환 후 리턴

    }



    public ShareDetailResponseDTO deleteBoard(int id, TokenUserInfo userInfo) {
            Share board = shareRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("대상이 존재하지 않아 삭제 실패!")
            );
            if(userInfo.getRole() != Role.ADMIN
                    && !userInfo.getUserId().equals(board.getUser().getId())){
                log.warn("작성자가 아니므로 삭제 불가"); // 관리자 또는 작성자만이 삭제 가능!
                throw new RuntimeException("작성 권한이 없습니다.");
            }

        List<Images> imagesList = imageRepository.findAllByBoardId(id);
        List<ShareComment> shareCommentList = shareCommentRepository.findAllByBoardId(id);

        List<ShareCommentResponseDTO> shareCommentResponseDTOList = new ArrayList<>();
        for(ShareComment shareComment:shareCommentList){
            ShareCommentResponseDTO shareCommentResponseDTO = ShareCommentResponseDTO.builder()
                    .content(shareComment.getContent())
                    .regDate(shareComment.getRegDate())
                    .userId(shareComment.getUser().getId())
                    .boardId(shareComment.getShare().getShareId())
                    .build();
            shareCommentResponseDTOList.add(shareCommentResponseDTO);
        }

            shareRepository.deleteById(id);

            return ShareDetailResponseDTO.builder()
                    .id(board.getShareId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .regDate(board.getRegDate())
                    .uploadImages(imagesList)
                    .comments(shareCommentResponseDTOList)
                    .approvalDate(board.getApprovalDate())
                    .approvalFlag(board.getApprovalFlag())
                    .userId(board.getUser().getId())
                    .build();
    }


    public List<ShareResponseDTO> getMyboardList(TokenUserInfo userInfo, ApprovalStatus approvalStatus) {
        if (approvalStatus == ApprovalStatus.APPROVE) {
            List<Share> boardList = shareRepository.findMyboardApprove(getUser(userInfo.getUserId()));

            List<ShareResponseDTO> dtoList = new ArrayList<>();
            for (Share board : boardList) {
                List<Images> imagesList = imageRepository.findAllByBoardId(board.getShareId());
                String filePath = imagesList.get(0).getFilePath();

                int countedComment = shareCommentRepository.countByBoard(board.getShareId());

                ShareResponseDTO dto = ShareResponseDTO.builder()
                        .id(board.getShareId())
                        .title(board.getTitle())
                        .regDate(board.getRegDate())
                        .userId(board.getUser().getId())
                        .imageUrl(filePath)
                        .commentCount(countedComment)
                        .content(board.getContent())
                        .approvalDate(board.getApprovalDate())
                        .userName(userRepository.findById(board.getUser().getId()).map(User::getUserName).orElse(null))
                        .build();
                dtoList.add(dto);
            }
            return dtoList;
        } else if (approvalStatus == ApprovalStatus.REJECT) {
            List<Share> boardList = shareRepository.findMyboardReject(getUser(userInfo.getUserId()));

            List<ShareResponseDTO> dtoList = new ArrayList<>();
            for (Share board : boardList) {
                List<Images> imagesList = imageRepository.findAllByBoardId(board.getShareId());
                String filePath = imagesList.get(0).getFilePath();

                int countedComment = shareCommentRepository.countByBoard(board.getShareId());

                ShareResponseDTO dto = ShareResponseDTO.builder()
                        .id(board.getShareId())
                        .title(board.getTitle())
                        .regDate(board.getRegDate())
                        .userId(board.getUser().getId())
                        .imageUrl(filePath)
                        .commentCount(countedComment)
                        .content(board.getContent())
                        .approvalDate(board.getApprovalDate())
                        .userName(userRepository.findById(board.getUser().getId()).map(User::getUserName).orElse(null))
                        .build();
                dtoList.add(dto);
            }
            return dtoList;
        } else {
            List<Share> boardList = shareRepository.findMyboardHold(getUser(userInfo.getUserId()));

            List<ShareResponseDTO> dtoList = new ArrayList<>();
            for (Share board : boardList) {
                List<Images> imagesList = imageRepository.findAllByBoardId(board.getShareId());
                String filePath = imagesList.get(0).getFilePath();

                int countedComment = shareCommentRepository.countByBoard(board.getShareId());

                ShareResponseDTO dto = ShareResponseDTO.builder()
                        .id(board.getShareId())
                        .title(board.getTitle())
                        .regDate(board.getRegDate())
                        .userId(board.getUser().getId())
                        .imageUrl(filePath)
                        .commentCount(countedComment)
                        .content(board.getContent())
                        .userName(userRepository.findById(board.getUser().getId()).map(User::getUserName).orElse(null))
                        .build();
                dtoList.add(dto);
            }
            return dtoList;
        }
    }
}


