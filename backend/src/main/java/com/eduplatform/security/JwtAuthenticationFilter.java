package com.eduplatform.security;

import com.eduplatform.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * Intercepts requests to validate JWT tokens and set authentication context
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            } catch (Exception e) {
                logger.error("JWT Token has expired or is invalid");
            }
        } else {
            // Check if token is provided without Bearer prefix
            if (requestTokenHeader != null && !requestTokenHeader.isEmpty()) {
                jwtToken = requestTokenHeader;
                try {
                    username = jwtUtil.getUsernameFromToken(jwtToken);
                } catch (Exception e) {
                    logger.error("JWT Token validation failed");
                }
            }
        }

        // Once we get the token validate it and set authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                // Validate token
                if (jwtUtil.validateToken(jwtToken)) {
                    // Get user details
                    var userOpt = userService.findByEmail(username);
                    if (userOpt.isPresent()) {
                        var user = userOpt.get();

                        // Create UserDetails-like object
                        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                                .username(user.getEmail())
                                .password(user.getPassword())
                                .authorities("ROLE_" + user.getRole().name())
                                .build();

                        // Create authentication token
                        UsernamePasswordAuthenticationToken authToken = 
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set authentication in security context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                logger.error("JWT Authentication failed: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Skip filter for public endpoints
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/auth/") || 
               path.startsWith("/public/") || 
               path.startsWith("/courses/public/") ||
               path.startsWith("/actuator/");
    }
}