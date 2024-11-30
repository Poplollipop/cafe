package com.cafe.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cafe.demo.POJO.Category;

public interface CategoryService {

    ResponseEntity<String> addCategory(Map<String, String> requestMap);

    ResponseEntity<List<Category>> getAllCategory(String filiterValue);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap);
    
    

}
