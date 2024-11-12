package com.cafe.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.demo.POJO.User;

public interface UserDao extends JpaRepository<User,Integer> {


    
}
