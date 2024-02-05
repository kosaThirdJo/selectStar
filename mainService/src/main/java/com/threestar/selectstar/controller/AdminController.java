package com.threestar.selectstar.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin/users")
    public ResponseEntity<?> getAllUsers(Pageable pageable){

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
