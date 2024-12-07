package com.cafe.demo.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.demo.JWT.JwtFilter;
import com.cafe.demo.POJO.Category;
import com.cafe.demo.POJO.Product;
import com.cafe.demo.constents.CafeConstants;
import com.cafe.demo.dao.ProductDao;
import com.cafe.demo.service.ProductService;
import com.cafe.demo.utils.CafeUtils;
import com.cafe.demo.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    JwtFilter jwtFilter;

    /**
     * 新增一個新的產品
     * 
     * @param requestMap 產品資料的 Map，包含產品的名稱、類別ID、描述、價格等
     * @return 返回操作結果，包含成功或錯誤訊息
     */
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            // 檢查是否為管理員
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            // 驗證產品資料
            if (validateProductMap(requestMap, false)) {
                // 轉換為 Product 物件並儲存
                productDao.save(getProductFromMap(requestMap, false));
                return CafeUtils.getResponseEntity("產品新增成功！", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 從 Map 轉換為 Product 物件
     * 
     * @param requestMap 產品資料的 Map
     * @param isAdded    是否為新增產品
     * @return 轉換後的 Product 物件
     */
    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdded) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        Product product = new Product();
        if (isAdded) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            product.setStatus("true"); // 預設為啟用狀態
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;
    }

    /**
     * 驗證產品資料是否有效
     * 
     * @param requestMap 產品資料的 Map
     * @param validateId 是否需要驗證產品 ID
     * @return 如果資料有效，返回 true；否則返回 false
     */
    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        // 檢查必要欄位是否存在
        if (requestMap.containsKey("name") && requestMap.containsKey("categoryId") && requestMap.containsKey("price")) {
            if (validateId && !requestMap.containsKey("id")) {
                return false; // 如果需要id，但沒有提供，返回 false
            }
            return true;
        }
        return false;
    }

    /**
     * 獲取所有產品列表
     * 
     * @return 返回所有產品列表
     */
    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 更新產品資料
     * 
     * @param requestMap 包含產品更新資料的 Map
     * @return 返回操作結果，包含成功或錯誤訊息
     */
    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            // 檢查是否為管理員
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            // 驗證資料
            if (validateProductMap(requestMap, true)) {
                Optional<Product> op = productDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!op.isEmpty()) {
                    Product product = getProductFromMap(requestMap, true);
                    product.setStatus(op.get().getStatus()); // 保留原有的狀態
                    productDao.save(product);
                    return CafeUtils.getResponseEntity("產品更新成功！", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("產品id並不存在！", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 根據產品 ID 刪除產品
     * 
     * @param id 產品 ID
     * @return 返回操作結果，包含成功或錯誤訊息
     */
    @Override
    public ResponseEntity<String> deleteProudct(Integer id) {
        try {
            // 檢查是否為管理員
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            // 確認產品存在後刪除
            Optional op = productDao.findById(id);
            if (!op.isEmpty()) {
                productDao.deleteById(id);
                return CafeUtils.getResponseEntity("產品刪除成功！", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("產品id並不存在", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 更新產品狀態
     * 
     * @param requestMap 包含產品 ID 和更新後狀態的 Map
     * @return 返回操作結果，包含成功或錯誤訊息
     */
    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            // 檢查是否為管理員
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            // 確認產品存在並更新狀態
            Optional op = productDao.findById(Integer.parseInt(requestMap.get("id")));
            if (!op.isEmpty()) {
                productDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                return CafeUtils.getResponseEntity("產品更新狀態成功！", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("產品id並不存在", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 根據類別 ID 獲取產品列表
     * 
     * @param id 類別 ID
     * @return 返回符合類別的產品列表
     */
    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 根據產品 ID 獲取產品詳細資料
     * 
     * @param id 產品 ID
     * @return 返回產品的詳細資料
     */
    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
