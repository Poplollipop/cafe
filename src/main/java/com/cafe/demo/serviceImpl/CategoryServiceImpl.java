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
import com.cafe.demo.constents.CafeConstents;
import com.cafe.demo.dao.CategoryDao;
import com.cafe.demo.service.CategoryService;
import com.cafe.demo.utils.CafeUtils;
import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (validateCategoryMap(requestMap, false)) {
                categoryDao.save(getCategoryFormMap(requestMap, false));
                return CafeUtils.getResponseEntity("類別新增成功！", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private com.cafe.demo.POJO.Category getCategoryFormMap(Map<String, String> requestMap, Boolean isAdded) {
        com.cafe.demo.POJO.Category category = new com.cafe.demo.POJO.Category();
        if (isAdded) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filiterValue) {
        try {
            if (!Strings.isNullOrEmpty(filiterValue) && filiterValue.equalsIgnoreCase("true")) {
                log.info("Inside if");
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<List<Category>>(categoryDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                System.out.println("Unauthorized: User is not an admin.");
                return CafeUtils.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);                
            }
            if (jwtFilter.isAdmin()) {
                System.out.println("Authorized: User is: " + jwtFilter);
                if (validateCategoryMap(requestMap, true)) {
                    Optional<Category> op = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (op.isPresent()) {
                        categoryDao.save(getCategoryFormMap(requestMap, true));
                        return CafeUtils.getResponseEntity("類別更新成功！", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("類別id並不存在", HttpStatus.BAD_REQUEST);
                    }
                }

                return CafeUtils.getResponseEntity(CafeConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
