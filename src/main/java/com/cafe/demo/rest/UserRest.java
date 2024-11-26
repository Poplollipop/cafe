package com.cafe.demo.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/user") // 定義路由的基本路徑，所有方法將以"/user"作為前綴
public interface UserRest {

    @PostMapping(path = "/signup") // 定義處理註冊請求的路徑為"/signup"
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);
    // signUp方法，接收一個必須的請求體(requestBody)，並返回一個ResponseEntity，包裝返回的訊息（字符串）
    // requestMap包含註冊所需的資料，例如用戶名、密碼等

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody (required=true) Map<String,String> requestMap);

}
