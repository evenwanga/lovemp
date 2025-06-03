package com.lovemp.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 安全配置类，整合Spring Security与Sa-Token
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全过滤链配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // 禁用CSRF
            .authorizeHttpRequests(auth -> auth
                // 放行的API
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // 放行H2控制台
                .anyRequest().authenticated()
            )
            // 允许H2控制台使用frame
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'self'"))
                .addHeaderWriter((request, response) -> {
                    response.setHeader("X-Frame-Options", "SAMEORIGIN");
                })
            );
        
        return http.build();
    }

    /**
     * 注册Sa-Token拦截器
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 注册Sa-Token拦截器
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/auth/**");
    }
} 