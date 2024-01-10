package com.threestar.selectstar.repository;

import com.threestar.selectstar.domain.entity.Apply;
import com.threestar.selectstar.domain.entity.ApplyID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.NONE)
@DataJpaTest
class ApplyRepositoryTest {

    @Autowired
    ApplyRepository applyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MeetingRepository meetingRepository;
    @Test
    @DisplayName("해당 글 신청 목록")
    void findByApplyID_Meeting_MeetingIdIs(){
        List<Apply> byApplyIDMeetingMeetingIdIs = applyRepository.findByApplyID_Meeting_MeetingIdIs(8);
        System.out.println(byApplyIDMeetingMeetingIdIs);
    }

    @Test
    @DisplayName("내가 신청한 글 목록")
    void findByMeeting_MeetingIdIsAndUser_UserIdIs(){
        List<Apply> byApplyIDUserUserIdIs = applyRepository.findByApplyID_User_UserIdIs(1);
        System.out.println(byApplyIDUserUserIdIs);
    }
//    @Test
//    @DisplayName("글 신청 여부 조회")
//    void check(){
//        boolean byApplyIDUserUserIdIsAndApplyIDMeetingMeetingIdIs = applyRepository.existsByApplyID_User_UserIdIsAndApplyID_Meeting_MeetingIdIs(1, 8);
//        System.out.println(byApplyIDUserUserIdIsAndApplyIDMeetingMeetingIdIs);
//    }
    @Test
    @DisplayName("저장 하기")
    void save(){
        ApplyID applyID = new ApplyID(userRepository.findById(1).get(), meetingRepository.findById(9).get());
        Apply build = Apply.builder()
                .applyID(applyID)
                .reason("이유")
                .snsAddress("sns")
                .emailAddress("emailAdd")
                .build();
        applyRepository.save(build);
//        boolean byApplyIDUserUserIdIsAndApplyIDMeetingMeetingIdIs = applyRepository.existsByApplyID_User_UserIdIsAndApplyID_Meeting_MeetingIdIs(1, 9);

    }
}