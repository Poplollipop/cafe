package com.cafe.demo.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cafe.demo.wrapper.ProductWrapper;

/**
 * ProductRest介面，提供與產品相關的REST API操作
 */
@RequestMapping(path = "/product")
public interface ProductRest {

    /**
     * 新增一個產品
     * 
     * @param requestMap 請求的產品資料，以Map的形式傳遞
     * @return 返回操作結果的訊息
     */
    @PostMapping(path="/add")
    ResponseEntity<String> addNewProduct(@RequestBody Map<String,String> requestMap);

    /**
     * 取得所有產品的列表
     * 
     * @return 返回包含所有產品資訊的列表，封裝在ProductWrapper中
     */
    @GetMapping(path="/get")
    ResponseEntity<List<ProductWrapper>> getAllProduct();

    /**
     * 更新產品資料
     * 
     * @param requestMap 請求的更新資料，以Map的形式傳遞
     * @return 返回操作結果的訊息
     */
    @PostMapping(path="/update")
    ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String, String> requestMap);

    /**
     * 根據產品ID刪除產品
     * 
     * @param id 需要刪除的產品ID
     * @return 返回操作結果的訊息
     */
    @PostMapping(path="/delete/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable Integer id);

    /**
     * 更新產品狀態
     * 
     * @param requestMap 包含更新狀態所需的資料，以Map的形式傳遞
     * @return 返回操作結果的訊息
     */
    @PostMapping(path= "/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody Map<String, String> requestMap);

    /**
     * 根據類別ID取得產品列表
     * 
     * @param id 產品類別ID
     * @return 返回符合條件的產品列表，封裝在ProductWrapper中
     */
    @GetMapping(path = "/getByCategory/{id}")
    ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Integer id);

    /**
     * 根據產品ID取得詳細產品資訊
     * 
     * @param id 產品ID
     * @return 返回單一產品的詳細資訊，封裝在ProductWrapper中
     */
    @GetMapping(path = "/getById/{id}")
    ResponseEntity<ProductWrapper> getProductById(@PathVariable Integer id);

}
