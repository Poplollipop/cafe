package com.cafe.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cafe.demo.POJO.Category;

public interface CategoryService {

    /**
     * 用來新增一個類別的方法。
     * 
     * @param requestMap 存放類別資訊的 Map，包含類別的屬性（例如名稱等）。
     * @return 返回包含操作結果的 ResponseEntity 物件，包含成功或錯誤的訊息。
     */
    ResponseEntity<String> addCategory(Map<String, String> requestMap);

    /**
     * 用來查詢所有類別的方法。
     * 
     * @param filterValue 用來過濾查詢的字串（可選參數），例如可以用來搜尋特定名稱的類別。
     * @return 返回包含所有類別的列表（List<Category>）的 ResponseEntity 物件。
     */
    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    /**
     * 用來更新某個類別的方法。
     * 
     * @param requestMap 存放類別更新資訊的 Map，包含類別的屬性（例如名稱、ID 等）。
     * @return 返回包含操作結果的 ResponseEntity 物件，包含成功或錯誤的訊息。
     */
    ResponseEntity<String> updateCategory(Map<String, String> requestMap);
}
