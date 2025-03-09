package com.harshmithaiwala.expensetracking.expensetracking.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("🔒 Configuring Security Filter Chain...");

        http
                .cors(cors -> {
                    logger.info("🌐 Configuring CORS in security chain");
                    cors.configurationSource(corsConfigurationSource());
                })
                .csrf(csrf -> {
                    logger.info("🛡️ Disabling CSRF protection");
                    csrf.disable();
                })
                .sessionManagement(session -> {
                    logger.info("📝 Setting session management to STATELESS");
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(auth -> {
                    logger.info("🔑 Configuring authorization rules");
                    auth
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/auth/login").permitAll()
                            .requestMatchers("/auth/register").permitAll()
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/error").permitAll()
                            .anyRequest().authenticated();
                    logger.info("✅ Authorization rules configured");
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handling -> {
                    logger.info("🛡️ Configuring exception handling");
                    handling
                            .authenticationEntryPoint((request, response, authException) -> {
                                logger.error("🚫 Authentication failed: {}", authException.getMessage());
                                logger.error("Request details - Method: {}, URI: {}, Content-Type: {}, Origin: {}", 
                                    request.getMethod(), 
                                    request.getRequestURI(),
                                    request.getContentType(),
                                    request.getHeader("Origin"));

                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
                            })
                            .accessDeniedHandler((request, response, accessDeniedException) -> {
                                logger.error("🚫 Access denied: {}", accessDeniedException.getMessage());
                                logger.error("Request details - Method: {}, URI: {}, Content-Type: {}, Origin: {}", 
                                    request.getMethod(), 
                                    request.getRequestURI(),
                                    request.getContentType(),
                                    request.getHeader("Origin"));

                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"" + accessDeniedException.getMessage() + "\"}");
                            });
                });

        logger.info("✅ Security Filter Chain configuration completed");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        logger.info("🔐 Creating Authentication Manager");
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.info("🌐 Creating CORS configuration");
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow specific origins
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "http://127.0.0.1:4200",
            "https://expense-tracker-frontend-tool.netlify.app"
        ));
        logger.info("📍 Allowed Origins configured: {}", configuration.getAllowedOrigins());

        // Allow all common methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        logger.info("🛠️ Allowed Methods configured: {}", configuration.getAllowedMethods());

        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Headers",
            "Access-Control-Allow-Methods",
            "X-Requested-With"
        ));
        logger.info("📋 Allowed Headers configured: {}", configuration.getAllowedHeaders());

        // Expose headers
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Access-Control-Allow-Headers"
        ));
        logger.info("📤 Exposed Headers configured: {}", configuration.getExposedHeaders());

        // Allow credentials
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        logger.info("✅ CORS configuration completed");
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("🔑 Creating Password Encoder");
        return new BCryptPasswordEncoder();
    }
}
