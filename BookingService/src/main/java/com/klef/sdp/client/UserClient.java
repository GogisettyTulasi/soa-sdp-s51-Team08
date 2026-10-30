package com.klef.sdp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.klef.sdp.dto.UserResponse;

@FeignClient(name = "UserService")
public interface UserClient {

    @GetMapping("/user/display/{id}")
    UserResponse getuserById(@PathVariable("id") Long id);
}