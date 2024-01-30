package com.threestar.boardService.controller;

//import com.threestar.boardService.config.auth.CustomUserDetails;
import com.threestar.boardService.dto.ChatsDTO;
import com.threestar.boardService.service.ChatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
@RestController
public class ChatsBoardController {

    private final ChatsService chatsService;

    public ChatsBoardController (ChatsService chatsService) {
        this.chatsService = chatsService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> createChats(
//            @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestParam(value="chatsImg") MultipartFile imgFile
//                                         @RequestBody ChatsDTO.RequestDTO chatsRequestDto
    ) {

        try {
//            chatsService.addChats(chatsRequestDto, userDetails.getUserId(), imgFile);
            chatsService.addChats(imgFile);

//            return ResponseEntity.ok(userDTO);

        } catch (Exception e) {
//            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미지 등록에 실패했습니다.");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{chatsId}")
    public ResponseEntity<?> removeChats(@PathVariable(name = "chatsId") Long chatsId,
//                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody ChatsDTO.RequestDTO chatsRequestDto) {

//        chatsService.deleteChats(chatsId, userDetails.getUserId(), chatsRequestDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/{chatsId}")
    public ResponseEntity<?> modifyChats(@PathVariable(name = "chatsId") Long chatsId,
//                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody ChatsDTO.RequestDTO chatsRequestDto) {

//        chatsService.updateChats(chatsId, chatsRequestDto, userDetails.getUserId());

        return ResponseEntity.ok().build();
    }
}
