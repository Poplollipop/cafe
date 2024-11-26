package com.cafe.demo.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cafe.demo.service.CustomerUserDetailService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // 注入 JwtUtils 用來處理 JWT 相關的邏輯
    @Autowired
    private JwtUtils jwtUtils;

    // 注入服務，用來加載用戶詳情
    @Autowired
    private CustomerUserDetailService service;

    // 用於存儲 JWT 的 Claims 資料
    Claims claims = null;
    // 存儲當前用戶名
    private String username = null;

    /**
     * 這是核心方法，用來過濾每個進來的請求
     * @param request 來自客戶端的 HTTP 請求
     * @param response 伺服器回應
     * @param filterChain 過濾器鏈
     * @throws ServletException 異常處理
     * @throws IOException 異常處理
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 如果請求的 URL 是登錄、忘記密碼或者註冊頁面，則不需要進行 JWT 檢查，直接放行
        if (request.getServletPath().matches("/user/login|/user/forgetPassword|/user/signup")) {
            filterChain.doFilter(request, response);
        } else {
            // 從 HTTP 請求中獲取 "Authorization" 頭部
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;

            // 如果 "Authorization" 頭部存在且以 "Bearer " 開頭，則提取 JWT
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);  // 去掉 "Bearer " 前綴
                username = jwtUtils.extractUsername(token);  // 提取用戶名
                claims = jwtUtils.extractAllClaims(token);   // 提取所有的 claims
            }

            // 如果用戶名不為空並且當前請求沒有已經認證的用戶
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 加載用戶詳情
                UserDetails userDetails = service.loadUserByUsername(username);
                
                // 如果 JWT 令牌有效，則設置用戶認證信息
                if (jwtUtils.validateToken(token, userDetails)) {
                    // 創建認證的 Token
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // 設置 Web 請求詳情（如 IP 地址等）
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 設置 Spring Security 上下文中的認證信息
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            // 繼續執行過濾器鏈
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 判斷當前用戶是否是 admin 角色
     * @return 如果是 admin 角色，返回 true，否則返回 false
     */
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    /**
     * 判斷當前用戶是否是 user 角色
     * @return 如果是 user 角色，返回 true，否則返回 false
     */
    public boolean isUser() {
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }

    /**
     * 獲取當前用戶的用戶名
     * @return 當前用戶名
     */
    public String getCurrentUser() {
        return username;
    }

}
