package com.klef.sdp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	    @GetMapping("/")
	    public String User() {
	        return "User Service is running";
	    }
	}

