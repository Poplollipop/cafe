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
import com.cafe.demo.constents.CafeConstents;
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

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (validateProductMap(requestMap, false)) {
                productDao.save(getProductFromMap(requestMap, false));
                return CafeUtils.getResponseEntity("產品新增成功！", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdded) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        Product product = new Product();
        if (isAdded) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        // 檢查必要欄位是否存在
        if (requestMap.containsKey("name") && requestMap.containsKey("categoryId") && requestMap.containsKey("price")) {
            if (validateId && !requestMap.containsKey("id")) {
                return false; // 如果需要id，但沒有提供，返回false
            }
            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (validateProductMap(requestMap, true)) {
                Optional<Product> op = productDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!op.isEmpty()) {
                    Product product = getProductFromMap(requestMap, true);
                    product.setStatus(op.get().getStatus());
                    productDao.save(product);
                    return CafeUtils.getResponseEntity("產品更新成功！", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("產品id並不存在！", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProudct(Integer id) {
        try {
            if(!jwtFilter.isAdmin()){
                return CafeUtils.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if(jwtFilter.isAdmin()){
                Optional op = productDao.findById(id);
                if(!op.isEmpty()){
                    productDao.deleteById(id);
                    return CafeUtils.getResponseEntity("產品刪除成功！", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("產品id並不存在",HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if(!jwtFilter.isAdmin()){
                return CafeUtils.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if(jwtFilter.isAdmin()){
                Optional op = productDao.findById(Integer.parseInt(requestMap.get("id")));
                if(!op.isEmpty()){
                    productDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    return CafeUtils.getResponseEntity("產品更新狀態成功！",HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("產品id並不存在",HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
