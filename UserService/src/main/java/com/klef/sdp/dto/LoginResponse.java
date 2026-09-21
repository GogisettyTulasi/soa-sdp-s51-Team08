package com.klef.sdp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse
{
    private String token;
    private String message;
    private Long userId;
    private String name;
    private String role;
}