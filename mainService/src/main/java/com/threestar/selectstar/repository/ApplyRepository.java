package com.threestar.selectstar.repository;

import com.threestar.selectstar.entity.Apply;
import com.threestar.selectstar.entity.ApplyID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, ApplyID> {
    List<Apply> findByApplyID_Meeting_MeetingIdIs(Long meeting_meetingId);
    List<Apply> findByApplyID_User_UserIdIs(Integer user_userId);
    List<Apply> findByApplyID_Meeting_MeetingIdIsAndApplyStatusIsNot(Long applyID_meeting_meetingId,  int applyStatus);
    Optional<Apply> findByApplyID_User_UserIdIsAndApplyID_Meeting_MeetingIdIs(Integer applyID_user_userId, Long applyID_meeting_meetingId);
    Optional<Apply> findByApplyID_Meeting_MeetingIdIsAndApplyID_User_UserIdIsAndApplyStatusIs(Long applyID_meeting_meetingId, Integer applyID_user_userId,  int applyStatus);
    Integer countByApplyID_Meeting_MeetingIdIsAndApplyStatusIs(Long applyID_meeting_meetingId, int applyStatus);
}
