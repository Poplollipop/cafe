package com.cafe.demo.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.demo.POJO.Category;
import com.cafe.demo.constents.CafeConstents;
import com.cafe.demo.rest.CategoryRest;
import com.cafe.demo.service.CategoryService;
import com.cafe.demo.utils.CafeUtils;

@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    CategoryService service;

    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
        try {
            return service.addCategory(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filiterValue) {
        try {
            return service.getAllCategory(filiterValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
           return service.updateCategory(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
