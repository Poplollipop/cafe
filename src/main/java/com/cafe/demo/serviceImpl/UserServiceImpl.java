package com.cafe.demo.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cafe.demo.JWT.JwtFilter;
import com.cafe.demo.JWT.JwtUtils;
import com.cafe.demo.POJO.User;
import com.cafe.demo.constents.CafeConstents;
import com.cafe.demo.dao.UserDao;
import com.cafe.demo.service.CustomerUserDetailService;
import com.cafe.demo.service.UserService;
import com.cafe.demo.utils.CafeUtils;
import com.cafe.demo.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

// @Slf4j 由 Lombok 自動生成 log 記錄器
@Slf4j
// @Service 表示該類別是 Spring 的服務層組件
@Service
public class UserServiceImpl implements UserService {

    // 注入資料存取層，用於與資料庫交互
    @Autowired
    UserDao userDao;

    // 注入 Spring Security 提供的認證管理器
    @Autowired
    AuthenticationManager authenticationManager;

    // 注入自定義的用戶細節服務，處理額外的用戶細節邏輯
    @Autowired
    CustomerUserDetailService customerUserDetailService;

    // 注入 JWT 工具類，負責生成和驗證 Token
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JwtFilter jwtFilter;

    /**
     * 註冊新使用者
     */
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup{}", requestMap); // 記錄請求參數
        try {
            // 驗證註冊資料是否有效
            if (validateSignUpMap(requestMap)) {
                // 根據 Email 檢查使用者是否已經存在
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) { // 如果使用者不存在
                    // 保存新使用者到資料庫
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("成功註冊", HttpStatus.OK);
                } else {
                    // 如果 Email 已存在，返回錯誤訊息
                    return CafeUtils.getResponseEntity("Email 已經存在！", HttpStatus.BAD_REQUEST);
                }
            } else {
                // 如果請求資料不完整或無效，返回錯誤訊息
                return CafeUtils.getResponseEntity(CafeConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 捕獲並打印錯誤訊息
        }
        // 如果發生未知錯誤，返回伺服器內部錯誤
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 驗證註冊資料是否有效
     */
    private boolean validateSignUpMap(Map<String, String> requestMap) {
        // 確認必要的欄位是否存在
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }

    /**
     * 將請求資料轉換為 User 實例
     */
    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name")); // 設定姓名
        user.setContactNumber(requestMap.get("contactNumber")); // 設定聯絡電話
        user.setEmail(requestMap.get("email")); // 設定電子郵件
        user.setPassword(requestMap.get("password")); // 設定密碼
        user.setStatus("false"); // 預設狀態為未啟用
        user.setRole("user"); // 預設角色為普通使用者
        return user;
    }

    /**
     * 使用者登入
     */
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login"); // 記錄進入登入方法
        log.info("Request received: {}", requestMap);
        try {
            // 認證使用者的 Email 和密碼
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
            
            if (auth.isAuthenticated()) { // 如果認證成功
                String status = customerUserDetailService.getUserDetail().getStatus();
                log.info("User status: {}", status); // 記錄使用者狀態
                
                // 驗證使用者狀態是否已啟用
                if ("true".equalsIgnoreCase(status)) {
                    // 使用者已啟用，生成 JWT Token 並返回
                    String token = jwtUtils.generatedToken(
                            customerUserDetailService.getUserDetail().getEmail(),
                            customerUserDetailService.getUserDetail().getRole());
                    log.info("JWT Token generated for user: {}", requestMap.get("email"));
                    return new ResponseEntity<String>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
                } else {
                    // 使用者未啟用，返回待管理員審批的提示
                    log.warn("User {} status is not active, waiting for admin approval.", requestMap.get("email"));
                    return new ResponseEntity<String>("{\"message\":\"Waiting for admin approval\"}", HttpStatus.BAD_REQUEST);
                }
            } else {
                // 如果認證未通過，返回錯誤訊息
                log.warn("Authentication failed for user: {}", requestMap.get("email"));
                return new ResponseEntity<String>("{\"message\":\"Bad Credentials\"}", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // 捕獲異常並記錄錯誤
            log.error("Exception occurred during login: ", e);
            return new ResponseEntity<String>("{\"message\":\"An error occurred during login\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
       try {
           if(jwtFilter.isAdmin()){
            
           }else{
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
           }
       } catch (Exception e) {
        e.printStackTrace();
       }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
