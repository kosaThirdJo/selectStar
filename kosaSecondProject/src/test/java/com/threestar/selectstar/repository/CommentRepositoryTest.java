package com.threestar.selectstar.repository;

import com.threestar.selectstar.domain.entity.Comment;
import com.threestar.selectstar.domain.entity.Meeting;
import com.threestar.selectstar.domain.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.NONE)
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MeetingRepository meetingRepository;
    @BeforeEach
    @Transactional
    void db_init() {
        //given
        //user
        // 저장 테스트
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
        User user2 = User.builder()
                .aboutMe("gdㅇㅇㅇ")
                .email("starc21@naver.com")
                .interestFramework("_0_")
                .interestJob("_0_")
                .interestLanguage("_0_")
                .joinDate(Date.valueOf(LocalDate.now()))
                .location1("부산광역시")
                .location2("광주광역시")
                .name("minsu")
                .nickname("민수닉")
                .password("12345")
                .profileContent("ㅎㅇㅎㅇㅎㅇㅎ")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        //meeting
        Meeting meeting1 = Meeting.builder()
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
        Meeting meeting2 = Meeting.builder()
                .title("테스트")
                .user(user1)
                .description("내용입니다.")
                .applicationCount(0)
                .category(1)
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
        Meeting meeting3 = Meeting.builder()
                .title("test")
                .user(user2)
                .description("내용입니다.")
                .applicationCount(0)
                .category(2)
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

        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
        meetingRepository.save(meeting3);
        Comment comment1 = Comment.builder()
                .content("테스트 댓글입니다.")
                .user(user1)
                .meeting(meeting1)
                .build();
        commentRepository.save(comment1);

    }

    @DisplayName("댓글 조회")
    @Test
    void find(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> byMeetingMeetingIdIs = commentRepository.findByMeeting_MeetingIdIs(25, pageable);
        for (Object ele:
             byMeetingMeetingIdIs) {
            System.out.println(ele);
        }
    }
    // 내가 등록한 댓글 들

    // 댓글 추가

    // 댓글 삭제



}