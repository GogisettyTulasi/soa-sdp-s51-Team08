package com.klef.sdp.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.klef.sdp.dto.LoginRequest;
import com.klef.sdp.dto.LoginResponse;
import com.klef.sdp.dto.UserRequest;
import com.klef.sdp.dto.UserResponse;

public interface UserService
        extends UserDetailsService
{
    UserResponse saveUser(UserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(
            Long id,
            UserRequest request);

    void deleteUser(Long id);

    LoginResponse login(LoginRequest request);
}