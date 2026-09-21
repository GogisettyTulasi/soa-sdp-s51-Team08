package com.klef.sdp.dto;

import com.klef.sdp.entity.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequest
{
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
    
    @Pattern(regexp = "^[6-9]\\d{9}$",message = "Invalid Contact Number")
    private String contact;

    @NotNull(message = "Role is required")
    @Schema(
        description = "User role",
        allowableValues = {"ADMIN", "MANAGER", "USER"},
        example = "USER"
    )
   
    private Role role;
}