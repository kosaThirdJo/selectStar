package com.threestar.boardService.controller;

import com.threestar.boardService.dto.ChatsDTO;
import com.threestar.boardService.service.ChatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ChatsBoardController {

    private final ChatsService chatsService;

    public ChatsBoardController (ChatsService chatsService) {
        this.chatsService = chatsService;
    }

    public ResponseEntity<?> createChats(@PathVariable(name = "chatsId") Long chatsId,
                                         @PathVariable(name = "usersId") Long usersId,
//                                         @AuthenticationPrincipal NowUserDetails nowUserDetails,
                                         @RequestBody ChatsDTO.RequestDTO chatsRequestDto) {
        chatsService.addChats(chatsRequestDto, chatsId, usersId);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> removeChats() {

        return null;
    }

    public ResponseEntity<?> modifyChats() {

        return null;
    }
}
