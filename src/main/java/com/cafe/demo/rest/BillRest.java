package com.cafe.demo.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cafe.demo.POJO.Bill;

@RequestMapping(path = "bill") // 設定請求路徑為 "bill"
public interface BillRest {

    @PostMapping(path = "generateReport") // 設定 POST 請求路徑為 "generateReport"
    ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap); // 接收報告生成請求，傳回 String 形式的回應

    @GetMapping(path = "getBills")
    ResponseEntity<List<Bill>> getBills();

    @PostMapping(path = "getPdf")
    ResponseEntity<byte[]> getBillsPdf(@RequestBody Map<String, Object> requestMap);

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteBill(@PathVariable Integer id);
}
