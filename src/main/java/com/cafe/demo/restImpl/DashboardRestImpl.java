package com.cafe.demo.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.demo.rest.DashboardRest;
import com.cafe.demo.service.DashboardService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class DashboardRestImpl implements DashboardRest{

    @Autowired
    DashboardService dashbordService;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
       return dashbordService.getCount();
    }
    
}
