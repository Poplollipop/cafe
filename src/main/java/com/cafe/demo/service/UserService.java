package com.cafe.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

// 定義 UserService 介面，該介面負責處理與用戶相關的業務邏輯
public interface UserService {

    // 用戶註冊方法，根據傳入的參數處理註冊邏輯
    // requestMap 參數包含用戶的註冊資料，通常是鍵值對形式（如 username, password 等）
    // 返回一個 ResponseEntity 物件，封裝了註冊結果的訊息及 HTTP 狀態碼
    ResponseEntity<String> signUp(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);

}
