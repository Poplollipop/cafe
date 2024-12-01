package com.cafe.demo.serviceImpl;

import java.util.Map;

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
                                return CafeUtils.getResponseEntity("",HttpStatus.OK);
                            }
                            return CafeUtils.getResponseEntity(CafeConstents.INVALID_DATA,HttpStatus.BAD_REQUEST);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
                    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdded) {
                    Category category= new Category();
                    category.setId(Integer.parseInt(requestMap.get("categoryId")));
                    Product product = new Product();
                    if(isAdded){
                        product.setId(Integer.parseInt(requestMap.get("id")));
                    }else{
                        product.setStatus("true");
                    }
                    product.setCategory(category);
                    product.setName(requestMap.get("name"));
                    product.setDescription(requestMap.get("description"));
                    product.setPrice(Integer.parseInt("price"));
                    return product;
                    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name") && validateId) {
            if (requestMap.containsKey("id")) {
                return true;
            } else if (!validateId) {
                return true;

            }
        }
        return false;
    }
}
