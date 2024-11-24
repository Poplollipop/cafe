package com.cafe.demo.JWT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.cafe.demo.service.CustomerUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomerUserDetailService customerUserDetailService;

    @Autowired
    JwtFilter jwtFilter;

    public SecurityConfig(CustomerUserDetailService customerUserDetailService) {
        this.customerUserDetailService = customerUserDetailService;
    }

    // 配置 SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()) // 設置 CORS 配置
                .and()
                .csrf().disable() // 禁用 CSRF
                .authorizeRequests()
                .requestMatchers("/user/login", "/user/signup", "/user/forgotPassword").permitAll() // 設置這些路徑為公開，不需要認證
                .anyRequest().authenticated() // 其他請求需要身份驗證
                .and()
                .exceptionHandling() // 配置異常處理
                .and()
                .sessionManagement() // 配置會話管理
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 設置會話為無狀態（例如 JWT 認證）
        // 如果需要配置 Basic 認證
        http.httpBasic(); // 可以根據需要更換為其他驗證方式，如表單登錄、JWT 等

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 配置 AuthenticationManager 並使用 customerUserDetailService
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
