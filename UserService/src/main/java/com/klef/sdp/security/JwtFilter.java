
package com.klef.sdp.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.klef.sdp.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService service;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        
        System.out.println("JWT FILTER PATH: " + path);
        System.out.println("REQUEST URI: " + request.getRequestURI());
        
        

        // Public APIs
        boolean isPublicEndpoint =
                path.equals("/user/")
                || path.equals("/user/test")
                || path.equals("/user/login")
                || path.equals("/user/add")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");

        System.out.println("IS PUBLIC ENDPOINT: " + isPublicEndpoint);
        
        if (isPublicEndpoint) {
            chain.doFilter(request, response);
            return;
        }

        // Get Authorization header
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Extract JWT
        String token = header.substring(7).trim();

        try {
            // Extract username/email from JWT
            String username = jwtUtil.extractUsername(token);

            if (SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                // Load user from database
                UserDetails userDetails =
                        service.loadUserByUsername(username);

                // Validate token
                if (userDetails != null &&
                    jwtUtil.validateToken(token, userDetails)) {

                    // Create authentication with user's authorities
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    // Store authentication
                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
                } else {
                    sendErrorResponse(
                            response,
                            401,
                            "Invalid or expired token");
                    return;
                }
            }

        } catch (Exception e) {
            sendErrorResponse(
                    response,
                    401,
                    "Invalid token");
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendErrorResponse(
            HttpServletResponse response,
            int status,
            String message)
            throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse =
                "{\"error\":\"Unauthorized\","
                + "\"message\":\""
                + message
                + "\"}";

        response.getWriter().write(jsonResponse);
    }
}
