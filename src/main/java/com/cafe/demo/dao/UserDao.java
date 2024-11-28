package com.cafe.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cafe.demo.POJO.User;
import com.cafe.demo.wrapper.UserWrapper;

public interface UserDao extends JpaRepository<User,Integer> {
     @Query(name = "User.findByEmailId")
    User findByEmailId(@Param("email") String email);

    List<UserWrapper> getAllUser();
    
    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    List<String> getAllAdmin();
    
    
    User findByEmail(String email);

}
