package com.cafe.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cafe.demo.POJO.Product;
import com.cafe.demo.wrapper.ProductWrapper;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();


    @Modifying
    @Transactional
    @Query(name = "Product.updateProductStatus")
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

}
