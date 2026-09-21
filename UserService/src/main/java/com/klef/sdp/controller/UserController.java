package com.klef.sdp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.sdp.dto.UserRequest;
import com.klef.sdp.dto.UserResponse;
import com.klef.sdp.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private UserService service;

    @GetMapping("/")
    public String home()
    {
        return "User Service is Running";
    }
    
    
    
    // CREATE
    @PostMapping("/add")
    public ResponseEntity<UserResponse> addUser(
            @Valid @RequestBody UserRequest request)
    {
        return new ResponseEntity<>(
                service.saveUser(request),
                HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping("/displayall")
    public ResponseEntity<List<UserResponse>> displayAllUsers()
    {
        return ResponseEntity.ok(
                service.getAllUsers());
    }

    // READ ONE
    @GetMapping("/display/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id)
    {
        return ResponseEntity.ok(
                service.getUserById(id));
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request)
    {
        return ResponseEntity.ok(
                service.updateUser(id, request));
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id)
    {
        service.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully");
    }
}

