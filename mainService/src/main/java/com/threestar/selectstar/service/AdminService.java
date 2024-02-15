package com.threestar.selectstar.service;

import com.threestar.selectstar.dto.user.response.GetUsersListResponse;
import com.threestar.selectstar.entity.User;
import com.threestar.selectstar.exception.UserNotFoundException;
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
}
