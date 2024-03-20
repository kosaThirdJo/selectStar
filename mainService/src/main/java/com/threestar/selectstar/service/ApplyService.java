package com.threestar.selectstar.service;

import com.threestar.selectstar.entity.Apply;
import com.threestar.selectstar.entity.ApplyID;
import com.threestar.selectstar.dto.apply.request.ApplyRequest;
import com.threestar.selectstar.dto.apply.request.RejectApplyRequest;
import com.threestar.selectstar.dto.apply.response.ApplyCheckResponse;
import com.threestar.selectstar.dto.apply.response.FindApplyByMeetingIdResponse;
import com.threestar.selectstar.dto.apply.response.FindApplyByMeetingIdValidResponse;
import com.threestar.selectstar.dto.apply.response.FindApplyByUserIdResponse;
import com.threestar.selectstar.entity.Notification;
import com.threestar.selectstar.entity.User;
import com.threestar.selectstar.repository.ApplyRepository;
import com.threestar.selectstar.repository.MeetingRepository;
import com.threestar.selectstar.repository.NotificationRepository;
import com.threestar.selectstar.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.threestar.selectstar.entity.QApply.apply;

@Slf4j
@Service
public class ApplyService {
    final ApplyRepository applyRepository;
    final UserRepository userRepository;
    final MeetingRepository meetingRepository;
    final NotificationRepository notificationRepository;

    public ApplyService(ApplyRepository applyRepository, UserRepository userRepository, MeetingRepository meetingRepository, NotificationRepository notificationRepository) {
        this.applyRepository = applyRepository;
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
        this.notificationRepository = notificationRepository;
    }

    // 지원 하기
    @Transactional
    public String addApply(ApplyRequest applyRequest) {
        try {
            applyRequest.setApplyStatus(0);
            applyRepository.save(ApplyRequest.toEntity(applyRequest,
                    new ApplyID(userRepository.findById(applyRequest.getUserId()).orElseThrow(IllegalArgumentException::new),
                            meetingRepository.findById(applyRequest.getMeetingId()).orElseThrow(IllegalArgumentException::new))));
            String title = meetingRepository.findById(applyRequest.getMeetingId()).orElseThrow(IllegalArgumentException::new).getTitle();
            // TODO 글신청자에게 알림 추가
            Notification notification = Notification.builder()
                    .notification_content(title + " 글에 신청자가 있습니다.")
                    .notification_type(1)
                    .user(userRepository.findByUserId(
                            meetingRepository.findById(applyRequest.getMeetingId())
                                    .orElseThrow(IllegalArgumentException::new)
                                    .getUser().getUserId()))
                    .notification_url("/meet/" + applyRequest.getMeetingId())
                    .build();
            notificationRepository.save(notification);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    // 지원 확인
    public ApplyCheckResponse checkApply(int userId, Long meetingId) {
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
    public List<FindApplyByMeetingIdResponse> findApplyByMeetingId(Long meetingId) {
        return applyRepository.findByApplyID_Meeting_MeetingIdIs(meetingId)
                .stream().map(FindApplyByMeetingIdResponse::fromEntity)
                .collect(Collectors.toList());
    }
    // 거절 안 된 글에서 지원한 사람
    public List<FindApplyByMeetingIdValidResponse> findApplyByMeetingIdValid(Long meetingId) {
        return applyRepository.findByApplyID_Meeting_MeetingIdIsAndApplyStatusIs(meetingId,0)
                .stream().map(FindApplyByMeetingIdValidResponse::fromEntity)
                .collect(Collectors.toList());
    }
    // 거절 시키기 0 대기 1 거절 2 수락
    @Transactional
    public String rejectApply(RejectApplyRequest rejectApplyRequest){
        Apply apply = applyRepository.findByApplyID_Meeting_MeetingIdIsAndApplyID_User_UserIdIsAndApplyStatusIs(rejectApplyRequest.getMeetingId()
                , rejectApplyRequest.getUserId(), 0).orElseThrow(IllegalArgumentException::new);
        apply.setApplyStatus(1);
        apply.setRejectReason(rejectApplyRequest.getReason());
        String title = meetingRepository.findById(rejectApplyRequest.getMeetingId()).orElseThrow(IllegalArgumentException::new).getTitle();
        Notification notification = Notification.builder()
                .notification_content(title + "글의 신청이 거절되었습니다.")
                .user(userRepository.findByUserId(rejectApplyRequest.getUserId()))
                .notification_type(2)
                .notification_url("/meet/" + rejectApplyRequest.getMeetingId())
                .build();
        notificationRepository.save(notification);



        return "success";
    }
    @Transactional // TODO RejectApplyRequest 고치기
    public String recognizeApply(RejectApplyRequest rejectApplyRequest){
        Apply apply = applyRepository.findByApplyID_Meeting_MeetingIdIsAndApplyID_User_UserIdIsAndApplyStatusIs(rejectApplyRequest.getMeetingId()
                , rejectApplyRequest.getUserId(), 0).orElseThrow(IllegalArgumentException::new); // 해당 부분에 문제가 있음.
        apply.setApplyStatus(2);
        apply.setRejectReason("");
        String title = meetingRepository.findById(rejectApplyRequest.getMeetingId()).orElseThrow(IllegalArgumentException::new).getTitle();

        Notification notification = Notification.builder()
                .notification_content(title + "글에 신청이 수락되었습니다.")
                .user(userRepository.findByUserId(
                        apply.getApplyID().getUser().getUserId()))
                .notification_type(2)
                .notification_url("/meet/" + rejectApplyRequest.getMeetingId())
                .build();
        notificationRepository.save(notification);
        return "success";
    }

//    public Integer countApplyByMeeting(Long meetingId){
//        return applyRepository.countByApplyID_Meeting_MeetingIdIsAndRejectIs(meetingId,0);
//    }

}


