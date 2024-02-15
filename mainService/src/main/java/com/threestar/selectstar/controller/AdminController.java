package com.threestar.selectstar.controller;

import com.threestar.selectstar.dto.user.request.UpdateUserStatusRequest;
import com.threestar.selectstar.dto.user.response.GetUsersListResponse;
import com.threestar.selectstar.entity.User;
import com.threestar.selectstar.service.AdminService;
import com.threestar.selectstar.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 회원 전체 조회
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        List<GetUsersListResponse> users = adminService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // 회원 상태 변경
    @PatchMapping("/users")
    public ResponseEntity<?> updateUserStatus(@RequestBody UpdateUserStatusRequest request) {
        adminService.updateUserStatus(request.getUserId(), request.getUserStatus());
        return ResponseEntity.ok().build();
    }
}
