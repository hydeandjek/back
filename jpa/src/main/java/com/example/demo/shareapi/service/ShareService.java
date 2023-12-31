package com.example.demo.shareapi.service;

import com.example.demo.auth.TokenUserInfo;
import com.example.demo.boardapi.repository.CommentRepository;
import com.example.demo.shareapi.dto.ApproveDateDTO;
import com.example.demo.shareapi.dto.ShareDetailResponseDTO;
import com.example.demo.shareapi.dto.ShareRequestDTO;
import com.example.demo.shareapi.dto.ShareResponseDTO;
import com.example.demo.shareapi.entity.Images;
import com.example.demo.shareapi.entity.Share;
import com.example.demo.shareapi.entity.ShareComment;
import com.example.demo.shareapi.repository.ImageRepository;
import com.example.demo.shareapi.repository.ShareCommentRepository;
import com.example.demo.shareapi.repository.ShareRepository;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ShareService {

    private final ShareRepository shareRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ShareCommentRepository shareCommentRepository;

    public List<ShareResponseDTO> getBoardList() {
//        List<Board> list = BoardRepository.findAll(Sort.by(Sort.Direction.DESC, "boardid"));
        List<Share> boardList = shareRepository.findAll();


        List<ShareResponseDTO> dtoList = new ArrayList<>();
        for(Share board : boardList){
            List<Images> imagesList = imageRepository.findAllByBoardId(board.getShareId());
            String filePath = imagesList.get(0).getFilePath(); //게시글id에 따른 첫번째 이미지의 경로

            int countedComment = shareCommentRepository.countByBoard(board.getShareId());

            ShareResponseDTO dto = ShareResponseDTO.builder()
                                        .id(board.getShareId())
                                        .title(board.getTitle())
//                                        .category(category)
                                        .regDate(board.getRegDate())
                                        .approvalDate(null)
                                        .userId(board.getUser().getId())
                                        .imageUrl(filePath)
                                        .commentCount(countedComment)
                                        .build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    public ShareResponseDTO setApprovalDate(int shareId, ApproveDateDTO dto) {
        Share share = shareRepository.findById(shareId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 게시글이 없습니다.")
        );
        share.setApprovalDate(dto.getApproveDate());

        return ShareResponseDTO.builder()
                .id(share.getShareId())
                .title(share.getTitle())
//                                        .category(category)
                .regDate(share.getRegDate())
                .approvalDate(share.getApprovalDate())
                .userId(share.getUser().getId())
//                .imageUrl(filePath)
//                .commentCount(countedComment)
                .build();
    }

    public ShareDetailResponseDTO getBoard(int id) {
        Share share = shareRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 id의 나눔 게시글은 없습니다.")
        );

        ShareDetailResponseDTO dto = ShareDetailResponseDTO.builder()
                .id(share.getShareId())
                .title(share.getTitle())
                .content(share.getContent())
//                .attachmentUrls(
//                        share.getUploadImages()
//                                .stream()
//                                .map((images)->images.getFilePath())
//                                .collect(Collectors.toList())
//                )
                // Images 엔티티의 파일저장경로(스트링) 리스트 괄호안에 넣기
                .regDate(share.getRegDate())
                .userId(share.getUser().getId())
                .approvalFlag(share.isApprovalFlag())
                .build();

        return dto;
    }

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
//        List<Images> imagesArrayList = new ArrayList<>();
//        List<ShareComment> shareCommentList = new ArrayList<>();

        // 받은 dto -> entity로 생성
//        Share share = new Share(user, requestDTO.getTitle(), requestDTO.getContent());

        Share share = Share.builder()
                .title(requestDTO.getTitle())
//                .uploadImages(imagesArrayList)
                .content(requestDTO.getContent())
                .user(user)
//                .comments(shareCommentList)
                .approvalFlag(false)
                .build();
        int id = shareRepository.save(share).getShareId();

        if(!Objects.isNull(files)){
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

                images.setShare(share); // 이미지를 게시글과 연결
                imageRepository.save(images); // 이미지 테이블에 저장
//
                file.transferTo(new File(filePath)); // 로컬에 이미지 저장
            }
        }
//        log.info("share.getUploadImages(): {}",share.getUploadImages());
        // 등록
//        Share saved = shareRepository.save(requestDTO.toEntity(user));
//        saved.getUploadImages().forEach(images -> {
//            images.setShare(saved);
//            imageRepository.save(images);
//
//        });

        return id;
//
//        log.info("할 일 저장 완료! saved: {}", saved);
//        return getBoard(saved.getShareId());
//        return getBoard(requestDTO.toEntity(user).getCategory(), requestDTO.toEntity(user).getBoardId());
    }

    // 유저 조회 메서드
    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        return user;
    }


    public ShareDetailResponseDTO updateBoard(final ShareRequestDTO requestDTO,
                                              final String userId,
                                              int boardId) throws RuntimeException{
        log.info("게시글 수정 서비스 작동!");
        Share entityToBeApplied = requestDTO.toEntity(getUser(userId)); // 엔티티로 변환

        Share targetBoard = shareRepository.findById(boardId).orElseThrow(null);

//        targetBoard.setCategory(entityToBeApplied.getCategory());
        targetBoard.setTitle(entityToBeApplied.getTitle());
        targetBoard.setContent(entityToBeApplied.getContent());

        return getBoard(boardId); // dto로 변환 후 리턴


//        targetBoard.ifPresent(boardRepository::save); // null 아닌 경우에만 실행.

        // 수정 대상인 보드
//        User user = getUser(userId);
//        List<Board> boardList = boardRepository.findAllByUser(user);

//        return getBoard(requestDTO.getCategory(), requestDTO.getBoardId());


    }



    public ShareResponseDTO deleteBoard(int id, String userId) {
            Share board = shareRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("대상이 존재하지 않아 삭제 실패!")
            );
            if(!userId.equals(board.getUser().getId())){
                log.warn("작성자가 아니므로 삭제 불가");
                throw new RuntimeException("작성 권한이 없습니다.");
            }

            shareRepository.deleteById(id);

            return ShareResponseDTO.builder()
                    .id(board.getShareId())
                    .title(board.getTitle())
//                    .category(board.getCategory())
                    .regDate(board.getRegDate())
                    .userId(board.getUser().getId())
                    .build();
    }



}


