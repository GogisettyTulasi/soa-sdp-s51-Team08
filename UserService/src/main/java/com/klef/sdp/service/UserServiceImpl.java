package com.klef.sdp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.klef.sdp.dto.LoginRequest;
import com.klef.sdp.dto.LoginResponse;
import com.klef.sdp.dto.UserRequest;
import com.klef.sdp.dto.UserResponse;
import com.klef.sdp.entity.User;
import com.klef.sdp.repository.UserRepository;
import com.klef.sdp.security.JwtUtil;

@Service
public class UserServiceImpl
        implements UserService
{
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // CREATE
    @Override
    public UserResponse saveUser(
            UserRequest request)
    {
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setContact(request.getContact());
        user.setRole(request.getRole());

        User savedUser =
                repository.save(user);

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
    public UserResponse getUserById(
            Long id)
    {
        User user =
                repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                            "User not found"));

        return convertToResponse(user);
    }

    // UPDATE
    @Override
    public UserResponse updateUser(
            Long id,
            UserRequest request)
    {
        User user =
                repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                            "User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setContact(request.getContact());
        user.setRole(request.getRole());

        User updatedUser =
                repository.save(user);

        return convertToResponse(updatedUser);
    }

    // DELETE
    @Override
    public void deleteUser(Long id)
    {
        User user =
                repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                            "User not found"));

        repository.delete(user);
    }

    // LOGIN
    @Override
    public LoginResponse login(
            LoginRequest request)
    {
        User user =
                repository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                    new RuntimeException(
                            "Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()))
        {
            throw new RuntimeException(
                    "Invalid email or password");
        }

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String token =
                jwtUtil.generateToken(userDetails);

        return new LoginResponse(
                token,
                "Login successful",
                user.getUserId(),
                user.getName(),
                user.getRole().name()
        );
    }

    // Used by JwtFiltrer
    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException
    {
        User user =
                repository.findByEmail(email)
                .orElseThrow(() ->
                    new UsernameNotFoundException(
                            "User not found with email: "
                            + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    private UserResponse convertToResponse(
            User user)
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