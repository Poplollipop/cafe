package com.cafe.demo.JWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service  // 標註該類為Spring的服務類，Spring會自動掃描並管理此類
public class JwtUtils {

    private String secreat = "demo11";  // 用於簽名JWT的密鑰，實際部署時應該保護好該密鑰，避免暴露

    // 從JWT中提取用戶名（即Subject）
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);  // 調用extractClaims方法，提取Subject（用戶名）
    }

    // 從JWT中提取過期時間
    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);  // 調用extractClaims方法，提取過期時間
    }

    // 提取JWT中的自定義聲明，通過傳入的claimsResolver函數
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);  // 提取所有的Claims（聲明）
        return claimsResolver.apply(claims);  // 使用提供的函數提取所需的聲明
    }

    // 從JWT中提取所有的Claims（聲明）
    public Claims extractAllClaims(String token) {
        // 使用Jwts.parser()來解析JWT，並使用密鑰驗證其簽名
        return Jwts.parser().setSigningKey(secreat).parseClaimsJws(token).getBody();  // 返回JWT中的Claims
    }

    // 檢查JWT是否過期
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());  // 如果過期時間在當前時間之前，則返回true
    }

    // 生成JWT Token，並將用戶名和角色放入claims中
    public String generatedToken(String username, String role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);  // 在JWT中添加用戶角色信息
        return createToken(claims, username);  // 調用createToken方法創建JWT
    }

    // 創建JWT Token，設置了claims、subject（用戶名）、發行時間和過期時間，並進行簽名
    private String createToken(Map<String, Object> claims, String subject){
        // 使用HS256算法簽名JWT，並設置過期時間為10小時
        return Jwts.builder()
                   .setClaims(claims)  // 設置claims
                   .setSubject(subject)  // 設置subject（用戶名）
                   .setIssuedAt(new Date(System.currentTimeMillis()))  // 設置JWT的發行時間
                   .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 設置過期時間（10小時）
                   .signWith(SignatureAlgorithm.HS256, secreat)  // 使用HS256簽名，並使用預設密鑰
                   .compact();  // 生成JWT並返回
    }

    // 驗證JWT是否合法，根據用戶詳細信息和JWT的用戶名來進行比較，並檢查是否過期
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);  // 提取JWT中的用戶名
        // 如果用戶名相同並且Token未過期，則JWT是有效的
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
