package com.cafe.demo.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.demo.rest.DashbordRest;
import com.cafe.demo.service.DashbordService;

@RestController
public class DashbordRestImpl implements DashbordRest{

    @Autowired
    DashbordService dashbordService;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
       return dashbordService.getCount();
    }
    
}
