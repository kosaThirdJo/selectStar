package com.threestar.selectstar.domain.service;

import com.threestar.selectstar.domain.entity.Apply;
import com.threestar.selectstar.domain.entity.ApplyID;
import com.threestar.selectstar.dto.apply.request.ApplyRequest;
import com.threestar.selectstar.dto.apply.request.RejectApplyRequest;
import com.threestar.selectstar.dto.apply.response.ApplyCheckResponse;
import com.threestar.selectstar.dto.apply.response.FindApplyByMeetingIdResponse;
import com.threestar.selectstar.dto.apply.response.FindApplyByMeetingIdValidResponse;
import com.threestar.selectstar.dto.apply.response.FindApplyByUserIdResponse;
import com.threestar.selectstar.repository.ApplyRepository;
import com.threestar.selectstar.repository.MeetingRepository;
import com.threestar.selectstar.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApplyService {
    final ApplyRepository applyRepository;
    final UserRepository userRepository;
    final MeetingRepository meetingRepository;

    public ApplyService(ApplyRepository applyRepository, UserRepository userRepository, MeetingRepository meetingRepository) {
        this.applyRepository = applyRepository;
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
    }

    // 지원 하기
    @Transactional
    public String addApply(ApplyRequest applyRequest) {
        try {
            applyRepository.save(ApplyRequest.toEntity(applyRequest,
                    new ApplyID(userRepository.findById(applyRequest.getUserId()).orElseThrow(IllegalArgumentException::new),
                            meetingRepository.findById(applyRequest.getMeetingId()).orElseThrow(IllegalArgumentException::new))));
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    // 지원 확인
    public ApplyCheckResponse checkApply(int userId, int meetingId) {
        return ApplyCheckResponse.fromEntity(applyRepository.findByApplyID_User_UserIdIsAndApplyID_Meeting_MeetingIdIs(userId, meetingId)
                .orElseThrow(IllegalArgumentException::new));
        }

    // 내가 지원한 글들
    public List<FindApplyByUserIdResponse> findApplyByUserId(int userId) {
        return applyRepository.findByApplyID_User_UserIdIs(userId)
                .stream().map(FindApplyByUserIdResponse::fromEntity)
                .collect(Collectors.toList());

    }
    // 글에서 지원한 사람
    public List<FindApplyByMeetingIdResponse> findApplyByMeetingId(int meetingId) {
        return applyRepository.findByApplyID_Meeting_MeetingIdIs(meetingId)
                .stream().map(FindApplyByMeetingIdResponse::fromEntity)
                .collect(Collectors.toList());
    }
    // 거절 안 된 글에서 지원한 사람
    public List<FindApplyByMeetingIdValidResponse> findApplyByMeetingIdValid(int meetingId) {
        return applyRepository.findByApplyID_Meeting_MeetingIdIsAndRejectIs(meetingId,0)
                .stream().map(FindApplyByMeetingIdValidResponse::fromEntity)
                .collect(Collectors.toList());
    }
    // 거절 시키기
    @Transactional
    public String rejectApplyByUserIdAndMeetingId(RejectApplyRequest rejectApplyRequest){
        Apply byApplyIDMeetingMeetingIdIsAndApplyIDUserUserIdIsAndRejectIs = applyRepository.findByApplyID_Meeting_MeetingIdIsAndApplyID_User_UserIdIsAndRejectIs(rejectApplyRequest.getMeetingId()
                , rejectApplyRequest.getUserId(), 0);
        byApplyIDMeetingMeetingIdIsAndApplyIDUserUserIdIsAndRejectIs.setReject(1);
        byApplyIDMeetingMeetingIdIsAndApplyIDUserUserIdIsAndRejectIs.setRejectReason(rejectApplyRequest.getReason());
        return "success";
    }
    public Integer countApplyByMeeting(int meetingId){
        return applyRepository.countByApplyID_Meeting_MeetingIdIsAndRejectIs(meetingId,0);
    }

}


