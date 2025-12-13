package com.hangrong.backend.config;

import com.hangrong.backend.security.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFilterConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new JwtFilter());
        reg.addUrlPatterns("/*"); // Áp dụng cho tất cả, nhưng filter sẽ bỏ qua /auth/*
        reg.setOrder(1);
        return reg;
    }
}