package com.cafe.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cafe.demo.POJO.Product;
import com.cafe.demo.wrapper.ProductWrapper;

/**
 * ProductDao介面，負責操作與產品相關的資料庫表
 */
public interface ProductDao extends JpaRepository<Product, Integer> {

    /**
     * 取得所有產品資訊
     * 
     * @return 返回封裝為ProductWrapper的產品列表
     */
    List<ProductWrapper> getAllProduct();

    /**
     * 更新產品狀態
     * 
     * @param status 產品的新狀態
     * @param id     產品的ID
     * @return 更新成功的行數
     */
    @Modifying
    @Transactional
    @Query(name = "Product.updateProductStatus")
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    /**
     * 根據類別取得產品資訊
     * 
     * @param id 類別ID
     * @return 返回封裝為ProductWrapper的產品列表
     */
    // @Query(name = "Product.getProductByCategory")
    List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

    /**
     * 根據ID取得產品資訊
     * 
     * @param id 產品ID
     * @return 返回封裝為ProductWrapper的產品資訊
     */
    // @Query(name = "Product.getProductById")
    ProductWrapper getProductById(@Param("id") Integer id);

}
