package com.cafe.demo.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.demo.JWT.JwtFilter;

import com.cafe.demo.constents.CafeConstents; // 引入常量類，用於定義系統中的常量
import com.cafe.demo.dao.UserDao;
import com.cafe.demo.rest.UserRest; // 引入UserRest接口，用於定義RESTful接口
import com.cafe.demo.service.UserService; // 引入UserService服務，用於處理業務邏輯
import com.cafe.demo.utils.CafeUtils; // 引入工具類，用於公共方法和返回格式處理
import com.cafe.demo.wrapper.UserWrapper;

@RestController // 標註此類為REST控制器，讓Spring能自動識別並處理HTTP請求
public class UserRestImpl implements UserRest { // 實現UserRest接口，具體處理用戶註冊功能

    @Autowired
    UserService userService; // 注入UserService服務，用於調用註冊邏輯

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) { // 實現UserRest接口中的signUp方法，處理用戶註冊
        try {
            // 調用userService的signUp方法處理註冊邏輯，返回一個ResponseEntity
            ResponseEntity<String> response = userService.signUp(requestMap);
            return response; // 返回註冊結果
        } catch (Exception e) {
            e.printStackTrace(); // 打印異常堆棧信息，用於調試
        }
        // 捕獲異常後，返回一個包含錯誤訊息的ResponseEntity，狀態為500（內部伺服器錯誤）
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
public ResponseEntity<String> login(Map<String, String> requestMap) {
    try {
        // 呼叫 userService 的 login 方法進行用戶登入
        return userService.login(requestMap);
    } catch (Exception e) {
        // 如果發生異常，印出錯誤訊息
        e.printStackTrace();
    }
    // 如果發生錯誤，返回內部伺服器錯誤的回應
    return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
}

@Override
public ResponseEntity<List<UserWrapper>> getAllUser() {
    try {
        // 呼叫 userService 的 getAllUser 方法取得所有用戶資料
        return userService.getAllUser();
    } catch (Exception e) {
        // 如果發生異常，印出錯誤訊息
        e.printStackTrace();
    }
    // 如果發生錯誤，返回空的用戶列表並設置 HTTP 狀態為內部伺服器錯誤
    return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
}

@Override
public ResponseEntity<String> update(Map<String, String> requestMap) {
    try {
        // 呼叫 userService 的 update 方法更新用戶資料
        return userService.update(requestMap);
    } catch (Exception e) {
        // 如果發生異常，印出錯誤訊息
        e.printStackTrace();
    }
    // 如果發生錯誤，返回內部伺服器錯誤的回應
    return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
}

@Override
public ResponseEntity<String> checkToken() {
    try {
        // 呼叫 userService 的 checkToken 方法檢查令牌
        return userService.checkToken();
    } catch (Exception e) {
        // 如果發生異常，印出錯誤訊息
        e.printStackTrace();
    }
    // 如果發生錯誤，返回內部伺服器錯誤的回應
    return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
}

@Override
public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
    try {
        // 呼叫 userService 的 changePassword 方法來更改密碼
        return userService.changePassword(requestMap);
    } catch (Exception e) {
        // 如果發生異常，印出錯誤訊息
        e.printStackTrace();
    }
    // 如果發生錯誤，返回內部伺服器錯誤的回應
    return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
}

@Override
public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
    try {
        // 呼叫 userService 的 forgotPassword 方法來處理忘記密碼邏輯
        return userService.forgotPassword(requestMap);
    } catch (Exception e) {
        // 如果發生異常，印出錯誤訊息
        e.printStackTrace();
    }
    // 如果發生錯誤，返回內部伺服器錯誤的回應
    return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
}
}
