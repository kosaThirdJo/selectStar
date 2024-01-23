package com.threestar.selectstar.repository;

import com.threestar.selectstar.entity.Comment;
import com.threestar.selectstar.entity.MeetingMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingMarkRepository  extends JpaRepository<MeetingMark, Long> {
    Optional<MeetingMark> findMeetingMarkByMeeting_MeetingIdIsAndUsers_UserIdIs(Long meeting_meetingId, Integer users_userId);



}
