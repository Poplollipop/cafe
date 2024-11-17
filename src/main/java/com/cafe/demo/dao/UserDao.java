package com.cafe.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cafe.demo.POJO.User;

public interface UserDao extends JpaRepository<User,Integer> {
     @Query(name = "User.findByEmailId")
    User findByEmailId(@Param("email") String email);
    
}
