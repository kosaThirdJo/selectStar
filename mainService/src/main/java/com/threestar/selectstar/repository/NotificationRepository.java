package com.threestar.selectstar.repository;

import com.threestar.selectstar.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findByUser_UserIdIs(Integer user_userId, Pageable pageable);
}
