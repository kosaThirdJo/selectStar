package com.threestar.boardService.controller;

import com.threestar.boardService.config.auth.CustomUserDetails;
import com.threestar.boardService.dto.ChatsDTO;
import com.threestar.boardService.service.ChatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class ChatsBoardController {

    private final ChatsService chatsService;

    public ChatsBoardController (ChatsService chatsService) {
        this.chatsService = chatsService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> createChats(@PathVariable(name = "chatsId") Long chatsId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody ChatsDTO.RequestDTO chatsRequestDto) {

        chatsService.addChats(chatsRequestDto, chatsId, userDetails.getUserId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{chatsId}")
    public ResponseEntity<?> removeChats(@PathVariable(name = "chatsId") Long chatsId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody ChatsDTO.RequestDTO chatsRequestDto) {

        chatsService.deleteChats(chatsId, userDetails.getUserId(), chatsRequestDto);

        return ResponseEntity.ok().build();
    }


    @PatchMapping("/update/{chatsId}")
    public ResponseEntity<?> modifyChats(@PathVariable(name = "chatsId") Long chatsId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody ChatsDTO.RequestDTO chatsRequestDto) {

        chatsService.updateChats(chatsRequestDto);

        return ResponseEntity.ok().build();
    }
}
