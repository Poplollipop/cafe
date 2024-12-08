package com.cafe.demo.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.demo.constents.CafeConstants;
import com.cafe.demo.rest.ProductRest;
import com.cafe.demo.service.ProductService;
import com.cafe.demo.utils.CafeUtils;
import com.cafe.demo.wrapper.ProductWrapper;

/**
 * ProductRestImpl類別，實現ProductRest介面，提供產品管理的具體操作
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ProductRestImpl implements ProductRest {

    @Autowired
    ProductService productService;

    /**
     * 新增產品
     * 
     * @param requestMap 產品的資料，通過Map傳遞
     * @return 返回操作結果訊息，成功時返回操作結果，失敗時返回錯誤訊息
     */
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            return productService.addNewProduct(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 若發生錯誤，返回內部伺服器錯誤訊息
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 獲取所有產品的列表
     * 
     * @return 返回所有產品的列表，若發生錯誤則返回空列表
     */
    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return productService.getAllProduct();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 若發生錯誤，返回空的產品列表
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 更新產品資訊
     * 
     * @param requestMap 包含更新產品資訊的資料
     * @return 返回操作結果訊息，成功時返回操作結果，失敗時返回錯誤訊息
     */
    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            return productService.updateProduct(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 若發生錯誤，返回內部伺服器錯誤訊息
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 根據產品ID刪除產品
     * 
     * @param id 需要刪除的產品ID
     * @return 返回操作結果訊息，成功時返回操作結果，失敗時返回錯誤訊息
     */
    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            return productService.deleteProudct(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 若發生錯誤，返回內部伺服器錯誤訊息
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 更新產品狀態
     * 
     * @param requestMap 包含更新狀態的資料
     * @return 返回操作結果訊息，成功時返回操作結果，失敗時返回錯誤訊息
     */
    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            return productService.updateStatus(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 若發生錯誤，返回內部伺服器錯誤訊息
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 根據類別ID取得產品列表
     * 
     * @param id 產品類別ID
     * @return 返回符合條件的產品列表，若發生錯誤則返回空列表
     */
    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            return productService.getByCategory(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 若發生錯誤，返回空的產品列表
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 根據產品ID取得詳細產品資訊
     * 
     * @param id 產品ID
     * @return 返回單一產品的詳細資訊，若發生錯誤則返回空的ProductWrapper
     */
    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return productService.getProductById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 若發生錯誤，返回空的產品資訊
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
