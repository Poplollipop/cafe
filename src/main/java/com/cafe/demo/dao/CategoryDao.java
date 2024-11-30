package com.cafe.demo.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.demo.POJO.Category;

public interface CategoryDao extends JpaRepository<Category, Integer> {

    List<Category> getAllCategory();

  

}
