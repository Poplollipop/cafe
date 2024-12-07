package com.cafe.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.demo.POJO.Bill;

public interface BillDao extends JpaRepository<Bill, Integer>{
    
}
