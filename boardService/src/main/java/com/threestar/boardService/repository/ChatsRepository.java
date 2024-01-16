package com.threestar.boardService.repository;

import com.threestar.boardService.entity.Chats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatsRepository extends JpaRepository<Chats, Integer> {

}
