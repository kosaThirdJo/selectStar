package com.threestar.selectstar.domain.service;

import com.threestar.selectstar.domain.entity.Meeting;
import com.threestar.selectstar.dto.comment.request.AddCommentRequest;
import com.threestar.selectstar.dto.comment.response.FindCommentResponse;
import com.threestar.selectstar.dto.meeting.response.FindMainPageResponse;
import com.threestar.selectstar.dto.meeting.response.FindMeetingOneResponse;
import com.threestar.selectstar.repository.ApplyRepository;
import com.threestar.selectstar.repository.CommentRepository;
import com.threestar.selectstar.repository.MeetingRepository;
import com.threestar.selectstar.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    final MeetingRepository meetingRepository;
    final UserRepository userRepository;
    final CommentRepository commentRepository;
    final ApplyRepository applyRepository;

    public CommentService(MeetingRepository meetingRepository, UserRepository userRepository,
                          CommentRepository commentRepository, ApplyRepository applyRepository) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.applyRepository = applyRepository;
    }
    // 해당 게시글 모든 댓글 조회
    public Page<FindCommentResponse> findComment(int meetingId, Pageable pageable) {
        return commentRepository.findByMeeting_MeetingIdIs(meetingId, pageable).map(FindCommentResponse::fromEntity);
    }
    // 댓글 등록
    public String addComment(AddCommentRequest addCommentRequest){
        try {
            commentRepository.save(AddCommentRequest.toEntity(addCommentRequest,
                    userRepository.findById(addCommentRequest.getUserId()).orElseThrow(IllegalArgumentException::new),
                    meetingRepository.findById(addCommentRequest.getMeetingId()).orElseThrow(IllegalArgumentException::new)));
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    // 댓글 삭제
    public String removeComment(int commentId){
        try {
            commentRepository.findById(commentId).orElseThrow(IllegalArgumentException::new).setDeleted(0);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    // 내가 등록한 모든 댓글
}
