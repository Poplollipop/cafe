package com.cafe.demo.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.demo.POJO.Category;
import com.cafe.demo.constents.CafeConstants;
import com.cafe.demo.rest.CategoryRest;
import com.cafe.demo.service.CategoryService;
import com.cafe.demo.utils.CafeUtils;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    CategoryService service;

     // 實現 CategoryRest 中的 addCategory 方法
     @Override
     public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
         try {
             // 呼叫 service 的 addCategory 方法來新增類別，並返回相應的 ResponseEntity
             return service.addCategory(requestMap);
         } catch (Exception e) {
             // 取得異常並印出錯誤
             e.printStackTrace();
         }
 
         // 返回錯誤訊息，表示發生了錯誤
         return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
     }
 
     // 實現 CategoryRest 中的 getAllCategory 方法
     @Override
     public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
         try {
             // 呼叫 service 的 getAllCategory 方法來獲取所有類別，並返回相應的 ResponseEntity
             return service.getAllCategory(filterValue);
         } catch (Exception e) {
             // 取得異常並印出錯誤
             e.printStackTrace();
         }
         // 返回空的類別列表，並回應 INTERNAL_SERVER_ERROR 錯誤狀態
         return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
     }
 
     // 實現 CategoryRest 中的 updateCategory 方法
     @Override
     public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
         try {
             // 呼叫 service 的 updateCategory 方法來更新類別，並返回相應的 ResponseEntity
             return service.updateCategory(requestMap);
         } catch (Exception e) {
             // 取得異常並印出錯誤
             e.printStackTrace();
         }
         // 返回錯誤訊息，表示發生了錯誤
         return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
     }
 }