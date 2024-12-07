package com.cafe.demo.restImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.demo.POJO.Bill;
import com.cafe.demo.constents.CafeConstants;
import com.cafe.demo.rest.BillRest;
import com.cafe.demo.service.BillService;
import com.cafe.demo.utils.CafeUtils;

@RestController  // 標註為 REST 控制器
public class BillRestImpl implements BillRest {

    @Autowired  // 注入 BillService 類別
    BillService billService;

    // 實現 generateReport 方法，處理生成報告的請求
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
       try {
        // 調用 billService 生成報告並返回結果
        return billService.generateReport(requestMap);
       } catch (Exception e) {
        // 若發生錯誤，輸出錯誤訊息
        e.printStackTrace();
       }
       // 若有錯誤，回傳自定義錯誤訊息及 500 內部伺服器錯誤狀態
       return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
        return billService.getBills();            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getBillsPdf(Map<String, Object> requestMap) {
       try {
        return billService.getBillsPdf(requestMap);
       } catch (Exception e) {
        e.printStackTrace();
       }
           return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            return billService.deleteBill(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
