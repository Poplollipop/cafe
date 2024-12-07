package com.cafe.demo.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    // 生成唯一的 UID，基於當前的時間戳
    public static String getUID() {
        Date date = new Date(); // 獲取當前日期時間
        long time = date.getTime(); // 獲取當前時間戳（自1970年以來的毫秒數）
        return "帳單-" + time; // 返回格式化的 UID
    }

    // 將字符串格式的 JSON 轉換為 JSONArray
    public static JSONArray getArrayFromString(String data) throws JSONException {
        JSONArray jsonArray = new JSONArray(data); // 將字符串轉換為 JSON 陣列
        return jsonArray; // 返回 JSONArray
    }

    /**
     * 將 JSON 字符串轉換為 Map 結構。
     * 如果輸入的字符串為空或 null，則返回一個空的 HashMap。
     *
     * @param data 輸入的 JSON 字符串
     * @return 轉換後的 Map<String, Object> 結構，如果字符串為空，返回空的 HashMap
     */
    public static Map<String, Object> getMapFromJson(String data) {
        // 檢查輸入的字符串是否為 null 或空字符串
        if (!Strings.isNullOrEmpty(data)) {
            // 使用 Gson 解析 JSON 並轉換為 Map<String, Object>
            return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
            }.getType());
        }
        // 如果輸入字符串無效，返回一個空的 HashMap
        return new HashMap<>();
    }
}
