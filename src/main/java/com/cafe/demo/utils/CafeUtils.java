package com.cafe.demo.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// 定義一個工具類，用於返回自定義的 HTTP 回應
public class CafeUtils {

    
    private CafeUtils() {

    }

    // 靜態方法：根據傳入的響應消息和 HTTP 狀態碼生成 ResponseEntity 對象
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        // 返回一個包含 JSON 格式消息和指定狀態碼的 ResponseEntity
        // 這裡的消息是封裝為 JSON 字符串形式，包含一個鍵為 "message" 的字段
        return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }

}
