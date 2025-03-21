package com.cafe.demo.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
import com.cafe.demo.constents.CafeConstants;
import com.cafe.demo.dao.UserDao;
import com.cafe.demo.service.CustomerUserDetailService;
import com.cafe.demo.service.UserService;
import com.cafe.demo.utils.CafeUtils;
import com.cafe.demo.utils.EmailUtils;
import com.cafe.demo.wrapper.UserWrapper;
import com.google.common.base.Strings;

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

    @Autowired
    EmailUtils emailUtils;

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
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 捕獲並打印錯誤訊息
        }
        // 如果發生未知錯誤，返回伺服器內部錯誤
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
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
                    return new ResponseEntity<String>("{\"message\":\"等待管理員授權！\"}", HttpStatus.BAD_REQUEST);
                }
            } else {
                // 如果認證未通過，返回錯誤訊息
                log.warn("Authentication failed for user: {}", requestMap.get("email"));
                return new ResponseEntity<String>("{\"message\":\"驗證失敗！\"}", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // 捕獲異常並記錄錯誤
            log.error("Exception occurred during login: ", e);
            return new ResponseEntity<String>("{\"message\":\"登入發生錯誤！\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            // 檢查當前用戶是否為管理員
            if (jwtFilter.isAdmin()) {
                // 如果是管理員，返回所有用戶的列表，狀態為 HTTP 200
                return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
            } else {
                // 如果不是管理員，返回空列表，狀態為 HTTP 401 (未授權)
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            // 捕捉異常並打印堆疊資訊
            e.printStackTrace();
        }
        // 如果出現異常，返回空列表，狀態為 HTTP 500 (伺服器內部錯誤)
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            // 檢查當前用戶是否為管理員
            if (jwtFilter.isAdmin()) {
                // 根據請求中的 ID 查找用戶
                Optional<User> op = userDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!op.isEmpty()) {
                    // 更新用戶的狀態
                    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));

                    // 通知所有管理員用戶狀態的變更
                    sendMailToAllAdmin(requestMap.get("status"), op.get().getEmail(), userDao.getAllAdmin());

                    // 返回成功響應，狀態為 HTTP 200
                    return CafeUtils.getResponseEntity("使用者狀態更新成功！", HttpStatus.OK);
                } else {
                    // 如果用戶不存在，返回對應訊息
                    return CafeUtils.getResponseEntity("使用者id，並不存在", HttpStatus.OK);
                }
            } else {
                // 如果當前用戶不是管理員，返回未授權響應
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            // 捕捉異常並打印堆疊資訊
            e.printStackTrace();
        }
        // 如果出現異常，返回伺服器內部錯誤
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 發送通知郵件給所有管理員
     * 
     * @param status   用戶的啟用狀態
     * @param user     被更新狀態的用戶郵箱
     * @param allAdmin 管理員的郵箱列表
     */
    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        // 移除當前管理員的郵箱，避免自己給自己發郵件
        allAdmin.remove(jwtFilter.getCurrentUser());

        // 判斷用戶是否啟用
        if (status != null && status.equalsIgnoreCase("true")) {
            // 如果啟用，發送啟用通知郵件
            emailUtils.sendSimpleMessage(
                    jwtFilter.getCurrentUser(),
                    "帳號啟用",
                    "使用者： " + user + " \n 啟用 \n 管理員：" + jwtFilter.getCurrentUser(),
                    allAdmin);
        } else {
            // 如果未啟用，發送未啟用通知郵件
            emailUtils.sendSimpleMessage(
                    jwtFilter.getCurrentUser(),
                    "帳號未啟用",
                    "使用者： " + user + " \n 未啟用 \n 管理員：" + jwtFilter.getCurrentUser(),
                    allAdmin);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            // 檢查 JWT token 是否有效，取得當前用戶的電子郵件
            String currentUserEmail = jwtFilter.getCurrentUser();
            System.out.println("Current User Email: " + currentUserEmail); // 輸出當前用戶的郵箱確認值

            // 如果當前用戶的電子郵件為空，表示 Token 缺失或無效，返回未授權錯誤
            if (currentUserEmail == null) {
                return CafeUtils.getResponseEntity("Token 缺失或無效", HttpStatus.UNAUTHORIZED);
            }

            // 根據當前用戶的電子郵件從資料庫查找用戶
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());

            // 如果用戶存在
            if (user != null) {
                // 檢查提供的舊密碼是否正確
                if (user.getPassword().equals(requestMap.get("oldPassword"))) {
                    // 如果舊密碼正確，更新為新密碼並保存
                    user.setPassword(requestMap.get("newPassword"));
                    userDao.save(user);
                    return CafeUtils.getResponseEntity("密碼更新成功！", HttpStatus.OK);
                }
                // 如果舊密碼不正確，返回錯誤提示
                return CafeUtils.getResponseEntity("舊密碼不正確！", HttpStatus.BAD_REQUEST);
            }

            // 如果找不到用戶，返回內部伺服器錯誤
            return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace(); // 印出錯誤信息
        }
        // 如果發生異常，返回內部伺服器錯誤
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            // 根據提供的電子郵件查找用戶
            User user = userDao.findByEmail(requestMap.get("email"));

            // 如果用戶存在且電子郵件有效，發送忘記密碼郵件
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgotMail(user.getEmail(), "來自管理系統的驗證", user.getPassword());

            // 返回提示用戶檢查認證信箱
            return CafeUtils.getResponseEntity("檢查你的認證信箱", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // 印出錯誤信息
        }
        // 如果發生異常，返回內部伺服器錯誤
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
