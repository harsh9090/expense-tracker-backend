package com.harshmithaiwala.expensetracking.expensetracking.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class); // ✅ Add Logger

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        logger.info("🔍 JwtFilter: Incoming request to {}", request.getRequestURI());

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            logger.warn("🚫 JwtFilter: No Bearer token found in request headers. Skipping authentication.");
            chain.doFilter(request, response);
            return;
        }

        token = token.substring(7); // Remove "Bearer " prefix
        logger.info("🔑 JwtFilter: Extracted JWT Token: {}", token);

        String email = jwtUtil.extractEmail(token);

        if (email == null) {
            logger.warn("🚫 JwtFilter: Could not extract email from token. Skipping authentication.");
            chain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("🛠️ JwtFilter: Loading user details for email: {}", email);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtUtil.validateToken(token)) {
                logger.info("✅ JwtFilter: JWT Token is valid. Setting authentication.");
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.warn("🚫 JwtFilter: JWT Token is invalid!");
            }
        } else {
            logger.info("🔒 JwtFilter: User already authenticated.");
        }

        chain.doFilter(request, response);
    }
}
