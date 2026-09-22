
package com.klef.sdp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.klef.sdp.dto.UserRequest;
import com.klef.sdp.dto.UserResponse;
import com.klef.sdp.dto.LoginRequest;
import com.klef.sdp.dto.LoginResponse;
import com.klef.sdp.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    // HOME - PUBLIC
    @GetMapping("/")
    public String home() {
        return "User Service is Running";
    }

    // TEST - PUBLIC
    @GetMapping("/test")
    public String test() {
        return "Test Api call";
    }

    // LOGIN - PUBLIC
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                service.login(request));
    }

    // CREATE USER - PUBLIC
    // Anyone can register
    @PostMapping("/add")
    public ResponseEntity<UserResponse> addUser(
            @Valid @RequestBody UserRequest request) {

        return new ResponseEntity<>(
                service.saveUser(request),
                HttpStatus.CREATED);
    }

    // READ ALL USERS - ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/displayall")
    public ResponseEntity<List<UserResponse>> displayAllUsers() {

        return ResponseEntity.ok(
                service.getAllUsers());
    }

    // READ ONE USER - ADMIN, USER, MANAGER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MANAGER')")
    @GetMapping("/display/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getUserById(id));
    }

    // UPDATE USER - ADMIN, USER, MANAGER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MANAGER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {

        return ResponseEntity.ok(
                service.updateUser(id, request));
    }

    // DELETE USER - ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        service.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully");
    }

    // OTHER APIs can also be protected according to roles.
}
