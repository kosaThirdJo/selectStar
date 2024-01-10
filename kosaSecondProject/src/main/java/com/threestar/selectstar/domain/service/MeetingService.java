package com.threestar.selectstar.domain.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.threestar.selectstar.domain.entity.Meeting;
import com.threestar.selectstar.dto.meeting.request.AddUpdateMeetingRequest;
import com.threestar.selectstar.dto.meeting.request.CompleteRequest;
import com.threestar.selectstar.dto.meeting.request.FindMainPageRequest;
import com.threestar.selectstar.dto.meeting.response.FindMeetingOneResponse;
import com.threestar.selectstar.dto.meeting.response.FindMainPageResponse;
import com.threestar.selectstar.dto.mypage.response.GetMyApplyingListResponse;
import com.threestar.selectstar.dto.mypage.response.GetMyMeetingListResponse;
import com.threestar.selectstar.repository.ApplyRepository;
import com.threestar.selectstar.repository.CommentRepository;
import com.threestar.selectstar.repository.MeetingRepository;
import com.threestar.selectstar.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.threestar.selectstar.domain.entity.QMeeting.meeting;

@Slf4j
@Service
public class MeetingService {

	final MeetingRepository meetingRepository;
	final UserRepository userRepository;
	final CommentRepository commentRepository;
	final ApplyRepository applyRepository;
	final JPAQueryFactory jpaQueryFactory;

	public MeetingService(MeetingRepository meetingRepository, UserRepository userRepository,
						  CommentRepository commentRepository, ApplyRepository applyRepository, JPAQueryFactory jpaQueryFactory) {
		this.meetingRepository = meetingRepository;
		this.userRepository = userRepository;
		this.commentRepository = commentRepository;
		this.applyRepository = applyRepository;
		this.jpaQueryFactory = jpaQueryFactory;
	}

	// 미팅 페이지를 조회한다.
	public Page<FindMainPageResponse> findMainPage(FindMainPageRequest findMainPageRequest) {
		// 총 페이지 랑 페이지 리스트 반환
		Pageable pageable;
		Page<Meeting> byDeletedIsOrderByCreationDateDesc;
		// 페이징 처리
		if (findMainPageRequest.getOrder() != null) {
			pageable = switch (findMainPageRequest.getOrder()) {
				case "desc" -> PageRequest.of(findMainPageRequest.getPage(),
					findMainPageRequest.getSize(),
					Sort.by(Sort.Direction.DESC,
						findMainPageRequest.getCriteria()));
				case "asc" -> PageRequest.of(findMainPageRequest.getPage(),
					findMainPageRequest.getSize(),
					Sort.by(Sort.Direction.ASC,
						findMainPageRequest.getCriteria()));
				default -> PageRequest.of(findMainPageRequest.getPage(),
					findMainPageRequest.getSize());
			};
		} else {
			pageable = PageRequest.of(findMainPageRequest.getPage(),
				findMainPageRequest.getSize());
		}
		if (findMainPageRequest.getCategory() == null)
			byDeletedIsOrderByCreationDateDesc = meetingRepository.findByDeletedIs(0,
				pageable);
		else
			byDeletedIsOrderByCreationDateDesc = meetingRepository.findByDeletedIsAndCategoryIs(0,
				findMainPageRequest.getCategory(),
				pageable);
		return byDeletedIsOrderByCreationDateDesc.map(entity -> FindMainPageResponse.fromEntity(
				entity,
			commentRepository.countByMeeting_MeetingIdIs(entity.getMeetingId()),
				applyRepository));
	}
	@Transactional
	public FindMeetingOneResponse findMeetingOne(int meetingId){
		// => 조회수 1 증가
		Meeting entity = meetingRepository.findById(meetingId).orElseThrow(IllegalArgumentException::new);
		entity.setViews(entity.getViews() + 1);
		return meetingRepository.findById(meetingId).
			map(meeting -> FindMeetingOneResponse.fromEntity(meeting,
							meeting.getUser().getNickname(),
					meeting.getUser().getAboutMe(),
					applyRepository.countByApplyID_Meeting_MeetingIdIsAndRejectIs(meetingId,0),
					null))
			.orElse(null);
	}

	@Transactional
	public String addMeeting(AddUpdateMeetingRequest addUpdateMeetingRequest){
		try {
			meetingRepository.save(AddUpdateMeetingRequest.toEntity
				(addUpdateMeetingRequest,userRepository.findById
						(addUpdateMeetingRequest.getUserId())
					.orElseThrow(IllegalArgumentException::new)));
			return "success";
		} catch (Exception e){
			return e.getMessage();
		}
	}
	@Transactional
	public String updateMeeting(AddUpdateMeetingRequest addUpdateMeetingRequest){
		try {
			AddUpdateMeetingRequest.meetingRequestUpdate(
				addUpdateMeetingRequest,
					meetingRepository.findById(addUpdateMeetingRequest.getMeetingId()).orElseThrow(IllegalArgumentException::new));

			return "success";
		} catch (Exception e){
			return e.getMessage();
		}
	}
	@Transactional
	public String removeMeeting(int id){
		try {
			meetingRepository.findById(id)
				.orElseThrow(IllegalArgumentException::new)
				.setDeleted(1);
			return "success";
		} catch (Exception e){
			return e.getMessage();
		}
	}
	@Transactional
	public String completeMeeting(CompleteRequest completeRequest){
		try {
			meetingRepository.findById(completeRequest.getMeetingId()).orElseThrow(IllegalArgumentException::new)
					.setStatus(1);
			return "success";
		} catch (Exception e){
			return e.getMessage();
		}
	}
	//내가 작성한 글 목록 조회[마이페이지]
	public List<GetMyMeetingListResponse> getMyMeetingList(int uId){
		//최신순 정렬
		Sort descSort = Sort.by(Sort.Direction.DESC, "creationDate");
		List<Meeting> myMeetingList = meetingRepository.findByUser_UserIdIsAndDeletedIs(uId, 0, descSort);
		//log.info("meetinglist entity  >>"+myMeetingList);
		if(myMeetingList.isEmpty()){
			return null;
		}else {
			List<GetMyMeetingListResponse> dtoList = new ArrayList<>();
			for (Meeting meeting : myMeetingList) {
				dtoList.add(GetMyMeetingListResponse.fromEntity(meeting, applyRepository));
			}
			//log.info("meetinglist dto  >>" + dtoList);
			return dtoList;
		}
	}

	//내가 작성한 글 목록 카테고리별/모집상태별 조회[마이페이지]
	public List<GetMyMeetingListResponse> getMyMeetingListByFilter(int uId, String strCategory, String strStatus){
		int category = 0, status=0;
		//카테고리 value(전체: all/프로젝트: project/스터디: study/기타: etc)
		if(strCategory != null){
			if(strCategory.equals("project")){
				category = 1;
			}else if(strCategory.equals("study")){
				category = 0;
			}else if(strCategory.equals("etc")){
				category = 2;
			}else if(strCategory.equals("all")){
				strCategory = null;
			}
		}
		//모집상태 value(전체: all/모집중: statusing/모집완료: statused)
		if(strStatus != null){
			if(strStatus.equals("statusing")){
				status = 0;
			}else if(strStatus.equals("statused")){
				status = 1;
			}else if(strStatus.equals("all")){
				strStatus = null;
			}
		}
		List<Meeting> entityList;
		if((strStatus != null && !strStatus.isEmpty()) && (strCategory != null && !strCategory.isEmpty())){
			entityList = meetingRepository.findByUser_UserIdIsAndDeletedIsAndCategoryIsAndStatusIs(uId, 0, category, status);
		}else if((strStatus == null) && (strCategory != null && !strCategory.isEmpty())){
			entityList = meetingRepository.findByUser_UserIdIsAndDeletedIsAndCategoryIs(uId, 0, category);
		}else if((strStatus != null && !strStatus.isEmpty()) && (strCategory == null)){
			entityList = meetingRepository.findByUser_UserIdIsAndDeletedIsAndStatusIs(uId, 0, status);
		}else{
			entityList = meetingRepository.findByUser_UserIdIsAndDeletedIs(uId, 0);
		}
		if(entityList.isEmpty()){
			return null;
		}else {
			List<GetMyMeetingListResponse> dtoList = new ArrayList<>();
			for (Meeting meeting :entityList) {
				dtoList.add(GetMyMeetingListResponse.fromEntity(meeting, applyRepository));
			}
			//log.info("meetinglist dto  >>" + dtoList);
			return dtoList;
		}
	}

	// 인기글 조회
	@Transactional(readOnly = true)
	public Page<FindMainPageResponse> getMeetingListRank() {

		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "views"));

		// 일주일 전 날짜
		LocalDate weekAgo = LocalDate.now().minusWeeks(1);

		Page<Meeting> topMeetings = meetingRepository.findByDeletedIsAndCreationDateIsGreaterThanEqual(0, Date.valueOf(weekAgo), pageable);

		return topMeetings.map(entity -> FindMainPageResponse.fromEntity(entity,
				commentRepository.countByMeeting_MeetingIdIs(entity.getMeetingId()),
				applyRepository));
	}

	// 모임글 검색 - 제목만
	 @Transactional(readOnly = true)
	public List<FindMainPageResponse> searchMeeting(String searchWord) {
		List<Meeting> searchMeeting = meetingRepository.findByTitleLikeAndDeletedIsOrderByCreationDateDesc("%" + searchWord + "%", 0);

		return searchMeeting.stream()
			.map(meeting -> FindMainPageResponse.fromEntity(meeting, commentRepository.countByMeeting_MeetingIdIs(meeting.getMeetingId()),applyRepository))
			.collect(Collectors.toList());
	}

	// 모임글 검색 - 필터링
/*	@Transactional(readOnly = true)
	public List<FindMainPageResponse> searchMeetingWithFilter(
		String searchWord, List<Integer> category, List<Integer> languages, List<Integer> frameworks, List<Integer> jobs) {
		List<Meeting> searchMeeting = new ArrayList<>();
		for(Integer lang : languages){
			for(Integer fw : frameworks){
				for(Integer job : jobs){
					searchMeeting.addAll(meetingRepository.findBySearchFilter(searchWord, 0, category, lang, fw, job));
				}
			}
		}

//		List<Meeting> meetings = meetingRepository.findBySearchFilter(searchWord, 0, category, languages, frameworks, jobs);
		return searchMeeting.stream()
			.map(meeting -> FindMainPageResponse.fromEntity(meeting, commentRepository.countByMeeting_MeetingIdIs(meeting.getMeetingId())))
			.collect(Collectors.toList());
	}*/
	@Transactional(readOnly = true)
	public List<FindMainPageResponse> searchMeetingWithFilter(String searchWord, List<Integer> category, List<Integer> languages, List<Integer> frameworks, List<Integer> jobs) {
		List<Meeting> searchMeeting = jpaQueryFactory.selectFrom(meeting)
				.where(
						meeting.title.containsIgnoreCase(searchWord),  // 제목에 검색어가 포함되어 있는지 (대소문자 구분 없이)
						meeting.deleted.eq(0),  // 삭제되지 않은(deleted = 0) 데이터만 검색
						searchCategoryCondition(category),
						searchLanguagesCondition(languages),
						searchFrameworksCondition(frameworks),
						searchJobsCondition(jobs)
				)
				.orderBy(meeting.creationDate.desc())
				.fetch();  // fetch()를 통해 검색 결과를 가져옴

		return searchMeeting.stream()
				.map(meeting -> FindMainPageResponse.fromEntity(meeting, commentRepository.countByMeeting_MeetingIdIs(meeting.getMeetingId()),applyRepository))
				.collect(Collectors.toList());
	}
	// BooleanExpression : QueryDsl에서 제공하는 논리적인 조건을 표현하는 타입
	// 각 메소드에서는 입력으로 받은 리스트가 null이 아닐 경우, 해당 리스트의 각 항목에 대해 meeting의 특정 필드가 해당 항목을 포함하고 있는지를 검사하는 BooleanExpression를 생성하고 이를 반환한다.
	// 만약 입력 리스트가 null일 경우, null을 반환
	private BooleanExpression searchCategoryCondition(List<Integer> category) {
		return category != null ? meeting.category.in(category) : null;
	}

	private BooleanExpression searchLanguagesCondition(List<Integer> languages) {
		if (languages != null) {
			BooleanExpression result = null;
			for (Integer language : languages) {
				if (result == null) {
					result = meeting.interestLanguage.contains(String.valueOf(language));
				} else {
					result = result.or(meeting.interestLanguage.contains(String.valueOf(language)));
				}
			}
			return result;
		}
		return null;
	}

	private BooleanExpression searchFrameworksCondition(List<Integer> frameworks) {
		if (frameworks != null) {
			BooleanExpression result = null;
			for (Integer framework : frameworks) {
				if (result == null) {
					result = meeting.interestFramework.contains(String.valueOf(framework));
				} else {
					result = result.or(meeting.interestFramework.contains(String.valueOf(framework)));
				}
			}
			return result;
		}
		return null;
	}

	private BooleanExpression searchJobsCondition(List<Integer> jobs) {
		if (jobs != null) {
			BooleanExpression result = null;
			for (Integer job : jobs) {
				if (result == null) {
					result = meeting.interestJob.contains(String.valueOf(job));
				} else {
					result = result.or(meeting.interestJob.contains(String.valueOf(job)));
				}
			}
			return result;
		}
		return null;
	}


	//내가 신청한 글 목록 조회[마이페이지]
    public List<GetMyApplyingListResponse> getMyApplyingList(int uId){

        List<Meeting> myApplyingList = meetingRepository.getMyApplyingList(uId, 0);
        //log.info("applyinglist entity  >>"+myApplyingList);
        if(myApplyingList.isEmpty()){
            return null;
        }else {
            List<GetMyApplyingListResponse> dtoList = new ArrayList<>();
            for (Meeting meeting : myApplyingList) {
                dtoList.add(GetMyApplyingListResponse.fromEntity(meeting, applyRepository));
            }
            //log.info("meetinglist dto  >>" + dtoList);
            return dtoList;
        }
    }
    //내가 신청한 글 목록 카테고리별/모집상태별 조회[마이페이지]
    public List<GetMyApplyingListResponse> getMyAppyingListByFilter(int uId, String strCategory, String strStatus){
        int category = 0, status=0;
        //카테고리 value(전체: all/프로젝트: project/스터디: study/기타: etc)
        if(strCategory != null){
            if(strCategory.equals("project")){
                category = 1;
            }else if(strCategory.equals("study")){
                category = 0;
            }else if(strCategory.equals("etc")){
                category = 2;
            }else if(strCategory.equals("all")){
                strCategory = null;
            }
        }
        //모집상태 value(전체: all/모집중: statusing/모집완료: statused)
        if(strStatus != null){
            if(strStatus.equals("statusing")){
                status = 0;
            }else if(strStatus.equals("statused")){
                status = 1;
            }else if(strStatus.equals("all")){
                strStatus = null;
            }
        }
        List<Meeting> entityList;
        if((strStatus != null && !strStatus.isEmpty()) && (strCategory != null && !strCategory.isEmpty())){
            entityList = meetingRepository.getMyApplyingListByCateAndSts(uId, 0, category, status);
        }else if((strStatus == null) && (strCategory != null && !strCategory.isEmpty())){
            entityList = meetingRepository.getMyApplyingListByCate(uId, 0, category);
        }else if((strStatus != null && !strStatus.isEmpty()) && (strCategory == null)){
            entityList = meetingRepository.getMyApplyingListByStatus(uId, 0, status);
        }else{
            entityList = meetingRepository.getMyApplyingList(uId, 0);
        }
        if(entityList.isEmpty()){
            return null;
        }else {
            List<GetMyApplyingListResponse> dtoList = new ArrayList<>();
            for (Meeting meeting :entityList) {
                dtoList.add(GetMyApplyingListResponse.fromEntity(meeting, applyRepository));
            }
            //log.info("meetinglist dto  >>" + dtoList);
            return dtoList;
        }
    }
	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void meetingScheduling(){
		try {
			// 조회 => 시간이 지난 경우 상태 1로 변경
			List<Meeting> byStatusIsAndApplicationDeadlineGreaterThanEqual = meetingRepository.findByStatusIsAndApplicationDeadlineLessThan(0, Date.valueOf(LocalDate.now()));
			for (Meeting meetingOne:
			byStatusIsAndApplicationDeadlineGreaterThanEqual) {
				meetingOne.setStatus(1);
			}
			// for문으로  수정
		} catch (Exception e){
			System.out.println("스케줄링 에러");
		}
	}
}
