package com.threestar.selectstar.repository;

import com.threestar.selectstar.domain.entity.ApplyID;
import com.threestar.selectstar.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByMeeting_MeetingIdIs(Integer meeting_meetingId, Pageable pageable);

    int countByMeeting_MeetingIdIs(Integer meeting_meetingId);
}
