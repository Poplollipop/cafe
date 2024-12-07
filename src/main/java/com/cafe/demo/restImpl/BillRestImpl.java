package com.cafe.demo.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.demo.constents.CafeConstents;
import com.cafe.demo.rest.BillRest;
import com.cafe.demo.service.BillService;
import com.cafe.demo.utils.CafeUtils;

@RestController
public class BillRestImpl implements BillRest {

    @Autowired
    BillService billService;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
       try {
        return billService.generateReport(requestMap);
       } catch (Exception e) {
        e.printStackTrace();
       }
       return CafeUtils.getResponseEntity(CafeConstents.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
