package com.threestar.selectstar.repository;

import com.threestar.selectstar.domain.entity.Meeting;
import com.threestar.selectstar.domain.entity.User;
import com.threestar.selectstar.dto.MeetingDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.NONE)
class MeetingRepositoryTest {

    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    @Transactional
    void db_init(){
        //given
        //user
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
    }

    @Order(1)
    @Test
    void 삭제_안된_모든미팅수(){
        //given before
        //when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Meeting> byDeletedIs = meetingRepository.findByDeletedIs(0,pageable);
        //then
        for (Object ele:
             byDeletedIs) {
            System.out.println(ele);
        }
        assertThat(byDeletedIs.getContent().size()).isEqualTo(3);
    }

    @Order(2)
    @Test
    void 삭제_안된_모든미팅_수(){
        //given before
        //when
        Integer count = meetingRepository.countByDeleted(0);
        //then
        assertThat(count).isEqualTo(3);
    }
    @Order(3)
    @Test
    void 삭제_안된_카테고리_미팅(){
        //given before
        List<Meeting> all = meetingRepository.findAll();
        for (Object ele:
             all) {
            System.out.println(ele);
        }
        //when
//        List<Meeting> byDeletedIsAndCategoryIs = meetingRepository.findByDeletedIsAndCategoryIsOrderByCreationDateDesc(0, 0,);
//        List<Meeting> byDeletedIsAndCategoryIs1 = meetingRepository.findByDeletedIsAndCategoryIsOrderByCreationDateDesc(0, 1);
//        List<Meeting> byDeletedIsAndCategoryIs2 = meetingRepository.findByDeletedIsAndCategoryIsOrderByCreationDateDesc(0, 2);
//        //then
//        assertThat(byDeletedIsAndCategoryIs.size()).isEqualTo(1);
//        assertThat(byDeletedIsAndCategoryIs1.size()).isEqualTo(1);
//        assertThat(byDeletedIsAndCategoryIs2.size()).isEqualTo(1);
    }
    @Order(4)
    @Test
    void 삭제_안된_카테고리_미팅_수(){
        //given before
        //when
        Integer i = meetingRepository.countByDeletedAndCategoryIsOrderByViewsDesc(0, 0);
        Integer i1 = meetingRepository.countByDeletedAndCategoryIsOrderByViewsDesc(0, 1);
        Integer i2 = meetingRepository.countByDeletedAndCategoryIsOrderByViewsDesc(0, 2);
        //then
        assertThat(i).isEqualTo(1);
        assertThat(i1).isEqualTo(1);
        assertThat(i2).isEqualTo(1);
    }
    @Order(5)
    @Test
    void 조회수_1올리기() {
        // given before
        // when
        Meeting meeting = meetingRepository.findAll().get(1);
        Integer count1 = meeting.getViews();
        meeting.setViews(meeting.getViews() + 1);
        meetingRepository.save(meeting);
        Integer count2 = meetingRepository.findById(meeting.getMeetingId()).get().getViews();
        // then
        assertThat(count1).isNotEqualTo(count2);
    }

    @Order(6)
    @Transactional
    @Test
    void 글_신청(){
        Meeting meeting4 = Meeting.builder()
                .title("테스트")
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
        meetingRepository.save(meeting4);
        assertThat(meeting4).isEqualTo(meetingRepository.findById(meeting4.getMeetingId()).get());
    }
    @Order(7)
    @Transactional
    @Test
    void 모집완료(){
        //when
        Meeting meeting = meetingRepository.findAll().get(0);
        meeting.setStatus(1);
        //then
        assertThat(meetingRepository.findById(meeting.getMeetingId()).get().getStatus()).isEqualTo(1);
    }
    @Order(8)
    @Transactional
    @Test
    void 게시글_삭제_상태_변경(){
        //when
        Meeting meeting = meetingRepository.findAll().get(0);
        meeting.setDeleted(1);
        //then
        assertThat(meetingRepository.findById(meeting.getMeetingId()).get().getDeleted()).isEqualTo(1);
    }
    @Order(9)
    @Transactional
    @Test
    void 메인_게시글조회(){
        //when 원래 4개지만 2개만 조회
        Pageable pageable = PageRequest.of(0, 2); // 0번째 페이지, 한 페이지당 2개의 결과
        Page<Meeting> byDeletedIs = meetingRepository.findByDeletedIs(0,pageable);
        //then
        assertThat(byDeletedIs.getContent().size()).isEqualTo(2);
    }
    @Order(10)
    @Test
    void 인기글_조회(){
        //when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Meeting> byDeletedIsAndCreationDateIsLessThan = meetingRepository.findByDeletedIsAndCreationDateIsGreaterThanEqual(0, Date.valueOf(LocalDate.now().minusDays(7)), pageable);
        Pageable pageable1 = PageRequest.of(0, 2);
        Page<Meeting> byDeletedIsAndCreationDateIsLessThan1 = meetingRepository.findByDeletedIsAndCreationDateIsGreaterThanEqual(0, Date.valueOf(LocalDate.now().minusDays(7)), pageable1);
        //then
        assertThat(byDeletedIsAndCreationDateIsLessThan.getContent().size()).isEqualTo(3);
        assertThat(byDeletedIsAndCreationDateIsLessThan1.getContent().size()).isEqualTo(2); //페이징 테스트
    }
    @Order(11)
    @Test
    void 모임글_검색_제목_일치(){
        // when
        List<Meeting> byTitleLikeAndDeletedIsOrderByCreationDateDesc = meetingRepository.findByTitleLikeAndDeletedIsOrderByCreationDateDesc("%테스트%", 0);
        // then
        assertThat(byTitleLikeAndDeletedIsOrderByCreationDateDesc.size()).isEqualTo(2);
    }
    @Order(12)
    @Test
    void 모임글_검색_필터링적용(){
        // Given
        String searchWord = "테스트";
        int deleted = 0;
        List<Integer> categoryList = Arrays.asList(1,3);
        String searchLanguage = "_0_";
        String searchFramework = "_0_2_";
        String searchJob = "_0_2_";

        // When
        List<String> searchLanguages = Arrays.asList(searchLanguage.split("_"));
        List<String> searchFrameworks = Arrays.asList(searchFramework.split("_"));
        List<String> searchJobs = Arrays.asList(searchJob.split("_"));

        List<Meeting> meetings = new ArrayList<>();

       /* for (String lang : searchLanguages) {
            for (String fw : searchFrameworks) {
                for (String job : searchJobs) {
                    *//*meetings.addAll(meetingRepository.findByTitleLikeAndDeletedAndCategoryInAndInterestLanguageContainingAndInterestFrameworkContainingAndInterestJobContainingOrderByCreationDateDesc(
                        "%"+searchWord+"%", deleted, categoryList, lang, fw, job));*//*
                    meetings.addAll(meetingRepository.findBySearchFilter(searchWord, deleted, categoryList, lang, fw, job));
                }
            }
        }*/

        // Then
        assertThat(meetings).isNotEmpty();
    }

    //@Order(13)
    @Test
    void 마이페이지_내가_작성한_글목록_조회(){
        //given
        User user = userRepository.findAll().get(0);
        //when
        List<Meeting> byUserUserIdIs = meetingRepository.findByUser_UserIdIsAndDeletedIs(3,0);
        //then
        byUserUserIdIs.stream().forEach(System.out::println);
        System.out.println(byUserUserIdIs);
       //assertThat(byUserUserIdIs.size()).isEqualTo(2);
    }
    @Order(14)
    @Test
    void 마이페이지_내가_작성한_글목록_조회_카테고리별(){
        // given
        User user = userRepository.findAll().get(0);
        // when
        List<Meeting> byUserUserIdIsAndDeletedIsAndCategoryIs = meetingRepository.findByUser_UserIdIsAndDeletedIsAndCategoryIs(user.getUserId(), 0, 0);
        // then
        assertThat(byUserUserIdIsAndDeletedIsAndCategoryIs.size()).isEqualTo(1);
    }
    @Order(15)
    @Test
    void 마이페이지_내가_작성한_글목록_조회_모집상태별(){
        //given
        User user = userRepository.findAll().get(0);
        // when
        List<Meeting> byUserUserIdIsAndDeletedIsAndStatusIs = meetingRepository.findByUser_UserIdIsAndDeletedIsAndStatusIs(user.getUserId(), 0, 0);
        // then
        assertThat(byUserUserIdIsAndDeletedIsAndStatusIs.size()).isEqualTo(2);

    }
    @Order(16)
    @Test
    void  마이페이지_내가_작성한_글목록_조회_카테고리별_모집상태별(){
        //given
        User user = userRepository.findAll().get(0);
        // when
        List<Meeting> byUserUserIdIsAndDeletedIsAndCategoryIsAndStatusIs = meetingRepository.findByUser_UserIdIsAndDeletedIsAndCategoryIsAndStatusIs(user.getUserId(), 0, 0, 0);
        // then
        assertThat(byUserUserIdIsAndDeletedIsAndCategoryIsAndStatusIs.size()).isEqualTo(1);
    }
    @Order(17)
    @Test
    void 신청시_신청인원증가(){
        //given
        Meeting meeting = meetingRepository.findAll().get(0);
        //when
        meeting.setApplicationCount(meeting.getApplicationCount()+1);
        //then
        assertThat(meetingRepository.findAll().get(0).getApplicationCount()).isNotEqualTo(0);
    }
    @Order(18)
    @Test
    void 게시글_수정(){
        //given
        Meeting meeting = meetingRepository.findAll().get(0);
        //when
        meeting.setInterestJob("_0_1_2_");
        meeting.setInterestJob("_0_3_");
        meeting.setDescription("내용변경합니다.");
        //then
        assertThat(meetingRepository.findAll().get(0)).isEqualTo(meeting);
        System.out.println(meeting);
    }
    @Order(19)
    @Transactional
    @Rollback(value = false)
    @Test
    void 테스트(){
        Meeting meeting = meetingRepository.findAll().get(0);
        Integer meetingId = meeting.getMeetingId();
        System.out.println(meeting);
        MeetingDTO meetingDTO = MeetingDTO.toDTO(meeting);
        meeting.setInterestJob("4e212");
        meetingDTO.setInterestJob("4e212");
        System.out.println(meetingRepository.findById(meetingId).get());
    }
    @Order(20)
    @Transactional
    @Test
    void 테스트1121(){
        Meeting meeting = meetingRepository.findAll().get(0);
        System.out.println(meeting);

    }

    //@Order(21)
    @Test
    void 내가_신청한_글목록_조회(){
        List<Meeting> applyingList = meetingRepository.getMyApplyingList(1, 0);
        applyingList.stream().forEach(System.out::println);

    }

}