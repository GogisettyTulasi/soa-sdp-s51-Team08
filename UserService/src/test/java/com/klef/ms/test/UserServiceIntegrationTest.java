package com.klef.ms.test;

import com.klef.sdp.UserServiceApplication;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = UserServiceApplication.class)
@AutoConfigureMockMvc
public class UserServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiscoveryClient discoveryClient;

    // COMMON METHOD: LOGIN AND GET JWT TOKEN

    private String getJWTToken() throws Exception {

    	String loginRequest = objectMapper.writeValueAsString(
    	        Map.of(
    	                "email", "tulasi@gmail.com",
    	                "password", "#gojo"
    	        )
    	);

    	MvcResult result = mockMvc.perform(
    	        post("/user/login")
    	                .contentType(MediaType.APPLICATION_JSON)
    	                .content(loginRequest)
    	)
    	.andReturn();

    	System.out.println("LOGIN STATUS: " +
    	        result.getResponse().getStatus());

    	System.out.println("LOGIN RESPONSE: " +
    	        result.getResponse().getContentAsString());

    	assertEquals(200, result.getResponse().getStatus());

        String response = result.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode tokenNode = jsonNode.get("token");

        assertNotNull(tokenNode, "JWT token was not generated");

        String token = tokenNode.asText();

        assertFalse(token.isBlank(), "JWT token is empty");

        return token;
    }

    // TEST CASE 1: VALID WORKFLOW
    // Login -> JWT -> Protected API -> Success

    @Test
    void testValidWorkflow() throws Exception {

        System.out.println("TEST CASE 1: VALID WORKFLOW");

        String token = getJWTToken();

        MvcResult result = mockMvc.perform(
                get("/user/displayall")
                        .header("Authorization", "Bearer " + token)
        )
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();

        assertNotNull(response);
        assertFalse(response.isBlank());

        System.out.println("JWT generated successfully");
        System.out.println("Status Code: " + result.getResponse().getStatus());
        System.out.println("Response: " + response);
        System.out.println("TC-1 PASSED");
    }

    // TEST CASE 2: INVALID JWT

    @Test
    void testInvalidJWT() throws Exception {

        System.out.println("TEST CASE 2: INVALID JWT");

        MvcResult result = mockMvc.perform(
                get("/user/displayall")
                        .header("Authorization", "Bearer invalid-jwt-token")
        )
        .andReturn();

        int statusCode = result.getResponse().getStatus();

        assertEquals(401, statusCode);

        System.out.println("Status Code: " + statusCode);
        System.out.println("TC-2 PASSED");
    }

    // TEST CASE 3: NO JWT TOKEN

    @Test
    void testNoJWTToken() throws Exception {

        System.out.println("TEST CASE 3: NO JWT TOKEN");

        MvcResult result = mockMvc.perform(
                get("/user/displayall")
        )
        .andReturn();

        int statusCode = result.getResponse().getStatus();

        assertEquals(401, statusCode);

        System.out.println("Status Code: " + statusCode);
        System.out.println("TC-3 PASSED");
    }

    // TEST CASE 4: EVENT SERVICE LOAD BALANCING

    @Test
    void testLoadBalancing() throws Exception {

        System.out.println("TEST CASE 4: EVENT SERVICE LOAD BALANCING");

        // Check Event Service instances in Eureka

        List<ServiceInstance> instances =
                discoveryClient.getInstances("EventService");

        System.out.println("EventService instances registered in Eureka:");

        for (ServiceInstance instance : instances) {

            System.out.println("Instance: " + instance.getInstanceId());
            System.out.println("Host: " + instance.getHost());
            System.out.println("Port: " + instance.getPort());
            System.out.println("URI: " + instance.getUri());
        }

        assertTrue(
                instances.size() >= 2,
                "At least two EventService instances must be running"
        );

        // Get JWT token

        String token = getJWTToken();

        // Send 10 requests to Event Service through User Service

        for (int i = 1; i <= 10; i++) {

            MvcResult result = mockMvc.perform(
                    get("/user/events")
                            .header("Authorization", "Bearer " + token)
            )
            .andExpect(status().isOk())
            .andReturn();

            System.out.println("Request " + i);
            System.out.println("Status: " + result.getResponse().getStatus());
            System.out.println("Response: " +
                    result.getResponse().getContentAsString());
        }

        System.out.println("TC-4 COMPLETED: EVENT SERVICE LOAD BALANCING");
    }

    // TEST CASE 5: EVENT SERVICE INSTANCE FAILURE
    //
    // MANUAL SETUP:
    // 1. Start Eureka.
    // 2. Start two EventService instances.
    // 3. Stop one EventService instance.
    // 4. Wait for Eureka to update.
    // 5. Run this test.

    @Test
    void testServiceDiscoveryFailure() throws Exception {

        System.out.println("TEST CASE 5: EVENT SERVICE INSTANCE FAILURE");

        // Check currently available Event Service instances

        List<ServiceInstance> instances =
                discoveryClient.getInstances("EventService");

        System.out.println("Available EventService instances:");

        for (ServiceInstance instance : instances) {

            System.out.println(
                    instance.getInstanceId() + " -> " + instance.getUri()
            );
        }

        assertTrue(
                instances.size() >= 1,
                "At least one EventService instance must be available"
        );

        // Get JWT token

        String token = getJWTToken();

        // Call Event Service through User Service

        MvcResult result = mockMvc.perform(
                get("/user/events")
                        .header("Authorization", "Bearer " + token)
        )
        .andReturn();

        int statusCode = result.getResponse().getStatus();

        String response = result.getResponse().getContentAsString();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response: " + response);

        assertEquals(200, statusCode);

        System.out.println("TC-5 PASSED: REQUEST SERVED BY AVAILABLE INSTANCE");
    }

    // TEST CASE 6: PUBLIC TEST API

    @Test
    void testPublicTestAPI() throws Exception {

        System.out.println("TEST CASE 6: PUBLIC TEST API");

        mockMvc.perform(
                get("/user/test")
        )
        .andExpect(status().isOk())
        .andExpect(content().string("Test Api call"));

        System.out.println("TC-6 PASSED: PUBLIC API");
    }
}
