package com.threestar.boardService.controller;

import com.threestar.boardService.service.ChatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class ChatsBoardController {

    private final ChatsService chatsService;

    public ChatsBoardController (ChatsService chatsService) {
        this.chatsService = chatsService;
    }

    public ResponseEntity<?> addChats() {
//        chatsService.
        return null;
    }

    public ResponseEntity<?> removeChats() {

        return null;
    }

    public ResponseEntity<?> modifyChats() {

        return null;
    }
}
