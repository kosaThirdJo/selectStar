package com.threestar.selectstar.service;

import com.threestar.selectstar.dto.comment.response.GetCommentsResponse;
import com.threestar.selectstar.dto.meeting.response.GetMeetingsResponse;
import com.threestar.selectstar.dto.user.response.GetUsersListResponse;
import com.threestar.selectstar.entity.Comment;
import com.threestar.selectstar.entity.Meeting;
import com.threestar.selectstar.entity.User;
import com.threestar.selectstar.exception.CommentNotFoundException;
import com.threestar.selectstar.exception.MeetingNotFoundException;
import com.threestar.selectstar.exception.UserNotFoundException;
import com.threestar.selectstar.repository.CommentRepository;
import com.threestar.selectstar.repository.MeetingRepository;
import com.threestar.selectstar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<GetUsersListResponse> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(GetUsersListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserStatus(Integer userId, int newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당하는 회원 없음"));
        user.updateUserStatus(newStatus);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<GetMeetingsResponse> getAllMeetings() {
        List<Meeting> meetingList = meetingRepository.findAll();
        return meetingList.stream()
                .map(GetMeetingsResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMeeting(long meetingId, int deleted) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException("해당하는 모임글 없음"));
        meeting.updateDeleted(deleted);
        meetingRepository.save(meeting);
    }

    @Transactional(readOnly = true)
    public List<GetCommentsResponse> getAllComments() {
        List<Comment> commentList = commentRepository.findAll();
        return commentList.stream()
                .map(GetCommentsResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(long commentId, int deleted) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("해당하는 댓글 없음"));
        comment.updateDeleted(deleted);
        commentRepository.save(comment);
    }
}
