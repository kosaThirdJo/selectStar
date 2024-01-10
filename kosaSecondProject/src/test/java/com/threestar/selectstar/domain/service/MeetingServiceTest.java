package com.threestar.selectstar.domain.service;

import com.threestar.selectstar.domain.entity.Meeting;
import com.threestar.selectstar.domain.entity.User;
import com.threestar.selectstar.repository.MeetingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.JUnitCore;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


// SpringJUnit4ClassRunner.class: JUnit의 확장 기능을 사용할 수 있도록 스프링에서 제공함
// 목적: 테스트코드를 실행 시 스프링 빈 컨테이너가 내부적으로 생성되도록 함 컨트롤러 webMvcTest
// 서비스레이어 유닛테스트 검색
@ExtendWith(SpringExtension.class)
class MeetingServiceTest {


    @InjectMocks
    private MeetingService meetingService;

    @MockBean

    private MeetingRepository meetingRepository;

    @Test
    void getPageMeetingDTO() {

        User user1 = User.builder()
                .aboutMe("gd")
                .email("starc13@naver.com")
                .interestFramework("_0_")
                .interestJob("_0_")
                .interestLanguage("_0_")
                .joinDate(Date.valueOf(LocalDate.now()))
                .location1("서울특별시")
                .location2("광주광역시")
                .name("sungsu")
                .nickname("성수닉")
                .password("12345")
                .profileContent("ㅎㅇㅎㅇㅎㅇㅎ")
                .build();
        Meeting meeting = Meeting.builder()
                .title("테스트")
                .user(user1)
                .description("내용입니다.")
                .applicationCount(0)
                .category(0)
                .applicationDeadline(Date.valueOf("2030-12-12"))
                .recruitmentCount(10)
                .location("서울특별시")
                .creationDate(Date.valueOf("2020-12-12"))
                .interestLanguage("_0_")
                .interestFramework("_0_")
                .interestJob("_0_")
                .status(0)
                .views(0)
                .build();



        //meeting 데이터를 목 리포지토리에 저장
        //when
        when(meetingRepository.save(any(Meeting.class))).thenReturn(meeting);
        when(meetingRepository.findById(1)).thenReturn(Optional.of(meeting));
        when(meetingRepository.findAll()).thenReturn(null);
    }

}