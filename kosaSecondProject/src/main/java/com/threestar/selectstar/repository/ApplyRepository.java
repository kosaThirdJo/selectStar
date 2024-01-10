package com.threestar.selectstar.repository;

import com.threestar.selectstar.domain.entity.Apply;
import com.threestar.selectstar.domain.entity.ApplyID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, ApplyID> {
    List<Apply> findByApplyID_Meeting_MeetingIdIs(Integer meeting_meetingId);
    List<Apply> findByApplyID_User_UserIdIs(Integer user_userId);
    List<Apply> findByApplyID_Meeting_MeetingIdIsAndRejectIs(Integer applyID_meeting_meetingId, int reject);
    Optional<Apply> findByApplyID_User_UserIdIsAndApplyID_Meeting_MeetingIdIs(Integer applyID_user_userId, Integer applyID_meeting_meetingId);
    Apply findByApplyID_Meeting_MeetingIdIsAndApplyID_User_UserIdIsAndRejectIs(Integer applyID_meeting_meetingId, Integer applyID_user_userId, int reject);
    Integer countByApplyID_Meeting_MeetingIdIsAndRejectIs(Integer applyID_meeting_meetingId, int reject);
}
