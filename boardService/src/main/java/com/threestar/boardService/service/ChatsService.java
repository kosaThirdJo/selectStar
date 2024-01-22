package com.threestar.boardService.service;

import com.threestar.boardService.dto.ChatsDTO;
import com.threestar.boardService.repository.ChatsRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatsService {

    private final ChatsRepository chatsRepository;

    public ChatsService (ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    public void addChats(ChatsDTO.RequestDTO chatsDTO, Long chatsId, Long usersId) {

        chatsRepository.save(chatsDTO.toEntity());
    }

    public void deleteChats(Long chatsId, Long userId, ChatsDTO.RequestDTO chatsDTO) {

        chatsDTO.toEntity().deleteChats(chatsDTO.getDeleted());
    }

    public void updateChats(ChatsDTO.RequestDTO chatsDTO) {

        chatsDTO.toEntity().updateChats(chatsDTO.getTitle(), chatsDTO.getContent());
    }
}
