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

    /**
     * 新增類別的方法
     * 
     * @param requestMap 包含新增類別的資料
     * @return 返回操作結果的 ResponseEntity
     */
    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
        try {
            // 驗證是否為管理員，如果不是管理員則返回未授權
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            // 驗證資料是否正確
            if (validateCategoryMap(requestMap, false)) {
                // 使用 CategoryDao 保存資料
                categoryDao.save(getCategoryFormMap(requestMap, false));
                return CafeUtils.getResponseEntity("類別新增成功！", HttpStatus.OK); // 回傳成功訊息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果發生錯誤，回傳錯誤訊息
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 驗證請求資料是否正確
     * 
     * @param requestMap 要驗證的資料
     * @param validateId 是否需要驗證ID
     * @return 返回驗證結果
     */
    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true; // 如果包含ID且需要驗證ID，則返回true
            } else if (!validateId) {
                return true; // 如果不需要驗證ID，則返回true
            }
        }
        return false; // 否則返回false
    }

    /**
     * 將 Map 中的資料轉換為 Category 物件
     * 
     * @param requestMap 來自請求的資料
     * @param isAdded    是否是新增操作
     * @return 返回 Category 物件
     */
    private com.cafe.demo.POJO.Category getCategoryFormMap(Map<String, String> requestMap, Boolean isAdded) {
        com.cafe.demo.POJO.Category category = new com.cafe.demo.POJO.Category();
        if (isAdded) {
            category.setId(Integer.parseInt(requestMap.get("id"))); // 設定ID
        }
        category.setName(requestMap.get("name")); // 設定名稱
        return category;
    }

    /**
     * 查詢所有類別的方法
     * 
     * @param filterValue 用來過濾查詢的字串
     * @return 返回類別列表
     */
    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            // 如果過濾條件為 "true"，則返回所有的類別
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                log.info("Inside if");
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
            }
            // 否則返回所有類別資料
            return new ResponseEntity<List<Category>>(categoryDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果發生錯誤，回傳空列表與錯誤訊息
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 更新類別的方法
     * 
     * @param requestMap 包含要更新的類別資料
     * @return 返回操作結果的 ResponseEntity
     */
    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            // 驗證是否為管理員，如果不是管理員則返回未授權
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            // 如果是管理員，繼續處理
            if (jwtFilter.isAdmin()) {
                System.out.println("Authorized: User is: " + jwtFilter);
                // 驗證資料是否正確
                if (validateCategoryMap(requestMap, true)) {
                    // 查詢類別是否存在
                    Optional<Category> op = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (op.isPresent()) {
                        // 如果類別存在，保存更新資料
                        categoryDao.save(getCategoryFormMap(requestMap, true));
                        return CafeUtils.getResponseEntity("類別更新成功！", HttpStatus.OK); // 返回成功訊息
                    } else {
                        return CafeUtils.getResponseEntity("類別id並不存在", HttpStatus.BAD_REQUEST); // 顯示類別ID不存在
                    }
                }

                return CafeUtils.getResponseEntity(CafeConstents.INVALID_DATA, HttpStatus.BAD_REQUEST); // 資料無效
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果發生錯誤，回傳錯誤訊息
        return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
