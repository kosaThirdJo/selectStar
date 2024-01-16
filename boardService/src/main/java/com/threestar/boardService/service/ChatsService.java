package com.threestar.boardService.service;

import com.threestar.boardService.repository.ChatsRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatsService {

    private final ChatsRepository chatsRepository;

    public ChatsService (ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    public boolean addChats() {


        return true;
    }
}
