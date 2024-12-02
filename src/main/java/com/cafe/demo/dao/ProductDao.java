package com.cafe.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.demo.POJO.Product;
import com.cafe.demo.wrapper.ProductWrapper;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();

    

}
