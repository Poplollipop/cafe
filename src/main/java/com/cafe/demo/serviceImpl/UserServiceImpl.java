package com.cafe.demo.serviceImpl;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cafe.demo.JWT.JwtUtils;
import com.cafe.demo.POJO.User;
import com.cafe.demo.constents.CafeConstents;
import com.cafe.demo.dao.UserDao;
import com.cafe.demo.service.CustomerUserDetailService;
import com.cafe.demo.service.UserService;
import com.cafe.demo.utils.CafeUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j // 使用Lombok的@Slf4j註解，會自動為這個類別創建一個名為 log 的 SLF4J Logger 實例
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao; // 注入UserDao，操作資料庫

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailService customerUserDetailService;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup{}", requestMap); // 記錄sign up的請求參數
        try {
            // 驗證請求參數是否有效
            if (validateSignUpMap(requestMap)) {
                // 根據Email查找是否已存在相同的使用者
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    // 如果沒有找到相同Email的使用者，則進行註冊
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("成功註冊", HttpStatus.OK); // 註冊成功返回OK
                } else {
                    // 如果Email已經存在，返回錯誤訊息
                    return CafeUtils.getResponseEntity("Email 已經存在！", HttpStatus.BAD_REQUEST);
                }
            } else {
                // 如果請求參數不完整或無效，返回錯誤訊息
                return CafeUtils.getResponseEntity(CafeConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 捕獲異常並打印錯誤堆疊
        }
        // 如果發生未知錯誤，返回500內部伺服器錯誤
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 驗證註冊資料的有效性
    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true; // 如果所有必要的資料都存在，則返回true
        }
        return false; // 否則返回false
    }

    // 從Map中提取資料並轉換為User對象
    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name")); // 設定使用者姓名
        user.setContactNumber(requestMap.get("contactNumber")); // 設定聯絡電話
        user.setEmail(requestMap.get("email")); // 設定電子郵件
        user.setPassword(requestMap.get("password")); // 設定密碼
        user.setStatus("false"); // 設定狀態為未啟用
        user.setRole("user"); // 設定角色為普通使用者
        return user; // 返回建立的User
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
            if (auth.isAuthenticated()) {
                if (customerUserDetailService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>(
                            "{\"token\":\"" + jwtUtils.generatedToken(
                                    customerUserDetailService.getUserDetail().getEmail(),
                                    customerUserDetailService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }
}
