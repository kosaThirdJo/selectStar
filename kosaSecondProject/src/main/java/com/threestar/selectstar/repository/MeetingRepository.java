package com.threestar.selectstar.repository;

import com.threestar.selectstar.domain.entity.Meeting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer>, QuerydslPredicateExecutor<Meeting> {

	// 삭제 안 된 전체 게시물 리스트 조회
	Page<Meeting> findByDeletedIs(int isDelete, Pageable pageable);

	//삭제 안 된 전체 게시물 수 조회
	Integer countByDeleted(int isDelete);

	// 카테고리가 지정된 삭제 안 된 게시글
	Page<Meeting> findByDeletedIsAndCategoryIs(int deleted, int category, Pageable pageable);

	// 카테고리가 지정된 삭제 안 된 게시글 수
	Integer countByDeletedAndCategoryIsOrderByViewsDesc(int deleted, int category);

	// 메인 - 인기글 조회 (RANK) : 최근 일주일간 올라온 글 중에서 조회수 높은 것 10개
	Page<Meeting> findByDeletedIsAndCreationDateIsGreaterThanEqual(int deleted, Date creationDate, Pageable pageable);

	// 모임글 검색
	List<Meeting> findByTitleLikeAndDeletedIsOrderByCreationDateDesc(String like, int deleted);

	// 모임글 검색 - 필터 적용
	/*@Query("SELECT m FROM Meeting m WHERE m.title LIKE %:searchWord% AND m.deleted = :deleted "
		+ "AND (:category IS NULL OR m.category IN :category) "
		+ "AND (:languages IS NULL OR m.interestLanguage LIKE %:languages%) "
		+ "AND (:frameworks IS NULL OR m.interestFramework LIKE %:frameworks%) "
		+ "AND (:jobs IS NULL OR m.interestJob LIKE %:jobs%) "
		+ "ORDER BY m.creationDate DESC")
	List<Meeting> findBySearchFilter(@Param("searchWord") String searchWord, @Param("deleted") int deleted,
		@Param("category") List<Integer> category, @Param("languages") Integer languages,
		@Param("frameworks") Integer frameworks, @Param("jobs") Integer jobs);*/
	// 내가 작성한 글 목록
	List<Meeting> findByUser_UserIdIsAndDeletedIs(Integer user_userId, int deleted);

	// 내가 작성한 글 목록(최신순)
	List<Meeting> findByUser_UserIdIsAndDeletedIs(Integer user_userId, int deleted, Sort sort);

	// 내가 작성한 글목록 조회(카테고리별)
	List<Meeting> findByUser_UserIdIsAndDeletedIsAndCategoryIs(Integer user_userId, int deleted, int category);

	// 내가 작성한 글목록 조회(모집상태별)
	List<Meeting> findByUser_UserIdIsAndDeletedIsAndStatusIs(Integer user_userId, int deleted, int status);

	// 내가 작성한 글목록 조회(카테고리별, 모집상태별)
	List<Meeting> findByUser_UserIdIsAndDeletedIsAndCategoryIsAndStatusIs(Integer user_userId, int deleted,
		int category, int status);

    //내가 신청한 글 목록
    @Query("select m, a from Meeting m join Apply a on m.meetingId = a.applyID.meeting.meetingId " +
            "where a.applyID.user.userId=:uid and m.deleted=:mds")
    public List<Meeting> getMyApplyingList(@Param("uid") int uid, @Param("mds") int mds);

    // 내가 작성한 글목록 조회(카테고리별)
    @Query("select m, a from Meeting m join Apply a on m.meetingId = a.applyID.meeting.meetingId " +
            "where a.applyID.user.userId=:uid and m.deleted=:mds and m.category=:cate")
    public List<Meeting> getMyApplyingListByCate(@Param("uid") int uid, @Param("mds") int mds, @Param("cate") int cate);

    // 내가 작성한 글목록 조회(모집상태별)
    @Query("select m, a from Meeting m join Apply a on m.meetingId = a.applyID.meeting.meetingId " +
            "where a.applyID.user.userId=:uid and m.deleted=:mds and m.status=:sts")
    public List<Meeting> getMyApplyingListByStatus(@Param("uid") int uid, @Param("mds") int mds, @Param("sts") int sts);

    // 내가 작성한 글목록 조회(카테고리별&모집상태별)
    @Query("select m, a from Meeting m join Apply a on m.meetingId = a.applyID.meeting.meetingId " +
            "where a.applyID.user.userId=:uid and m.deleted=:mds and m.category=:cate and m.status=:sts")
    public List<Meeting> getMyApplyingListByCateAndSts(@Param("uid") int uid, @Param("mds") int mds, @Param("cate") int cate, @Param("sts") int sts);

	// 현재 시각 보다 만료 시간이 적은 만료 안된 리스트
	public List<Meeting> findByStatusIsAndApplicationDeadlineLessThan(int status, Date applicationDeadline);
}
