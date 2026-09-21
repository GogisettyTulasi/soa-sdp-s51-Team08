package com.klef.sdp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.sdp.dto.UserRequest;
import com.klef.sdp.dto.UserResponse;
import com.klef.sdp.entity.User;
import com.klef.sdp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository repository;

    // CREATE
    @Override
    public UserResponse saveUser(UserRequest request)
    {
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setContact(request.getContact());
        user.setRole(request.getRole());


        User savedUser = repository.save(user);

        return convertToResponse(savedUser);
    }

    // READ ALL
    @Override
    public List<UserResponse> getAllUsers()
    {
        return repository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // READ ONE
    @Override
    public UserResponse getUserById(Long id)
    {
        User user = repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));

        return convertToResponse(user);
    }

    // UPDATE
    @Override
    public UserResponse updateUser(Long id, UserRequest request)
    {
        User user = repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setContact(request.getContact());
        user.setRole(request.getRole());

        User updatedUser = repository.save(user);

        return convertToResponse(updatedUser);
    }

    // DELETE
    @Override
    public void deleteUser(Long id)
    {
        User user = repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));

        repository.delete(user);
    }

    // Convert User Entity to UserResponse
    private UserResponse convertToResponse(User user)
    {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .contact(user.getContact())
                .role(user.getRole())
                .build();
    }
}

