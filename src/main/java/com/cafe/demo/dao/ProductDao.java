package com.cafe.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.demo.POJO.Product;

public interface ProductDao extends JpaRepository<Product, Integer> {

    

}
