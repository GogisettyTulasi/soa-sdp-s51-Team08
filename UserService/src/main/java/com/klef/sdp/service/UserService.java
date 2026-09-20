package com.klef.sdp.service;

import java.util.List;

import com.klef.sdp.dto.UserRequest;
import com.klef.sdp.dto.UserResponse;

public interface UserService
{
    // CREATE
    UserResponse saveUser(UserRequest request);

    // READ ALL
    List<UserResponse> getAllUsers();

    // READ ONE
    UserResponse getUserById(Long id);

    // UPDATE
    UserResponse updateUser(Long id, UserRequest request);

    // DELETE
    void deleteUser(Long id);
}
