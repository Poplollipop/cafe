package com.cafe.demo.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cafe.demo.service.CustomerUserDetailService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomerUserDetailService service;

    Claims claims = null;
    private String username = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getServletPath().matches("/user/login|/user/forgetPassword|/user/signup")){
            filterChain.doFilter(request, response);
        }else{
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;

            
        }
        
    }

}
