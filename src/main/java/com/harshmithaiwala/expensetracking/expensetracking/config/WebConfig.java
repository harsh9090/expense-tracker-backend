package com.harshmithaiwala.expensetracking.expensetracking.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class); // ✅ Add Logger

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                logger.info("🌍 Configuring CORS settings...");

                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:4200",
                                "https://expense-tracker-frontend-tool.netlify.app"
                        ) // ✅ Explicitly allow origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ✅ Allow all methods
                        .allowedHeaders("*") // ✅ Allow all headers
                        .exposedHeaders("Authorization")
                        .allowCredentials(true); // ✅ Allow credentials

                logger.info("✅ CORS configured successfully! Allowed Origins: [http://localhost:4200, https://expense-tracker-frontend-tool.netlify.app]");
            }
        };
    }
}
