package com.cafe.demo.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.demo.constents.CafeConstents; // 引入常量類，用於定義系統中的常量
import com.cafe.demo.rest.UserRest; // 引入UserRest接口，用於定義RESTful接口
import com.cafe.demo.service.UserService; // 引入UserService服務，用於處理業務邏輯
import com.cafe.demo.utils.CafeUtils; // 引入工具類，用於公共方法和返回格式處理
import com.cafe.demo.wrapper.UserWrapper;

@RestController // 標註此類為REST控制器，讓Spring能自動識別並處理HTTP請求
public class UserRestImpl implements UserRest { // 實現UserRest接口，具體處理用戶註冊功能

    @Autowired
    UserService userService; // 注入UserService服務，用於調用註冊邏輯

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
    public ResponseEntity<String> login(Map<String,String> requestMap) {
    try {
        return userService.login(requestMap);
    } catch (Exception e) {
        e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            return userService.getAllUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
       return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
