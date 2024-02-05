package com.threestar.boardService.service;

import com.threestar.boardService.dto.ChatsDTO;
import com.threestar.boardService.entity.User;
import com.threestar.boardService.repository.ChatsRepository;
import com.threestar.boardService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ChatsService {

    private final ChatsRepository chatsRepository;

    private final UserRepository userRepository;

    private final S3UploadService s3UploadService;

    public ChatsService (ChatsRepository chatsRepository, UserRepository userRepository, S3UploadService s3UploadService) {
        this.chatsRepository = chatsRepository;
        this.userRepository = userRepository;
        this.s3UploadService = s3UploadService;
    }

    public void addChats(ChatsDTO.RequestDTO chatsDTO, Long usersId, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findById(usersId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        String fileName = s3UploadService.saveFile(multipartFile);

        log.info("@@@@@@@@@@@@@ fileName : " + fileName);
        // TODO 게시글 이미지 파일 이름 저장 fileName
//        chatsRepository.save(chatsDTO.toEntity(user));
    }

    public void addChats(MultipartFile multipartFile) throws IOException {
//        User user = userRepository.findById(usersId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        String fileName = s3UploadService.saveFile(multipartFile);

        log.info("@@@@@@@@@@@@@ fileName : " + fileName);

//        chatsRepository.save(chatsDTO.toEntity(user));
    }

    public void deleteChats(Long chatsId, Long usersId, ChatsDTO.RequestDTO chatsDTO) {
        User user = userRepository.findById(usersId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        chatsDTO.toEntity(user).deleteChats(chatsDTO.getDeleted());
//        s3UploadService.deleteImage();
    }

    public void updateChats(Long chatsId, ChatsDTO.RequestDTO chatsDTO, Long usersId) {

        User user = userRepository.findById(usersId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        chatsDTO.toEntity(user).updateChats(chatsDTO.getTitle(), chatsDTO.getContent());
    }
}
