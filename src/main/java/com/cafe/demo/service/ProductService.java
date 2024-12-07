package com.cafe.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cafe.demo.wrapper.ProductWrapper;

/**
 * ProductService 介面，提供產品管理的業務邏輯操作
 */
public interface ProductService {

    /**
     * 新增一個產品
     * 
     * @param requestMap 產品的資料，通過 Map 形式傳遞
     * @return 返回操作結果訊息，成功時返回操作結果，失敗時返回錯誤訊息
     */
    ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

    /**
     * 獲取所有產品的列表
     * 
     * @return 返回所有產品的列表，封裝在 ProductWrapper 中
     */
    ResponseEntity<List<ProductWrapper>> getAllProduct();

    /**
     * 更新產品資料
     * 
     * @param requestMap 包含更新資料的 Map
     * @return 返回操作結果訊息，成功時返回操作結果，失敗時返回錯誤訊息
     */
    ResponseEntity<String> updateProduct(Map<String, String> requestMap);

    /**
     * 根據產品ID刪除產品
     * 
     * @param id 需要刪除的產品ID
     * @return 返回操作結果訊息，成功時返回操作結果，失敗時返回錯誤訊息
     */
    ResponseEntity<String> deleteProudct(Integer id);

    /**
     * 更新產品的狀態
     * 
     * @param requestMap 包含狀態更新資料的 Map
     * @return 返回操作結果訊息，成功時返回操作結果，失敗時返回錯誤訊息
     */
    ResponseEntity<String> updateStatus(Map<String, String> requestMap);

    /**
     * 根據類別ID取得產品列表
     * 
     * @param id 產品類別ID
     * @return 返回符合條件的產品列表，封裝在 ProductWrapper 中
     */
    ResponseEntity<List<ProductWrapper>> getByCategory(Integer id);

    /**
     * 根據產品ID取得產品詳細資料
     * 
     * @param id 產品ID
     * @return 返回單一產品的詳細資訊，封裝在 ProductWrapper 中
     */
    ResponseEntity<ProductWrapper> getProductById(Integer id);

}
