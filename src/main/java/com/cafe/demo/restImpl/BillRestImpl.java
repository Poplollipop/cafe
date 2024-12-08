package com.cafe.demo.restImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.demo.POJO.Bill;
import com.cafe.demo.constents.CafeConstants;
import com.cafe.demo.rest.BillRest;
import com.cafe.demo.service.BillService;
import com.cafe.demo.utils.CafeUtils;

@CrossOrigin(origins = "http://localhost:4200")
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
            // 使用 BillService 的 getBills 方法來獲取帳單列表
            return billService.getBills();
        } catch (Exception e) {
            // 捕獲任何異常並印出信息
            e.printStackTrace();
        }
        // 當發生異常時，返回空的 ResponseEntity
        return null;
    }
    
    @Override
    public ResponseEntity<byte[]> getBillsPdf(Map<String, Object> requestMap) {
        try {
            // 使用 BillService 的 getBillsPdf 方法來生成帳單 PDF
            return billService.getBillsPdf(requestMap);
        } catch (Exception e) {
            // 捕獲任何異常並印出信息
            e.printStackTrace();
        }
        // 當發生異常時，返回空的 ResponseEntity
        return null;
    }
    
    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            // 嘗試調用 BillService 的 deleteBill 方法來刪除指定的帳單
            return billService.deleteBill(id);
        } catch (Exception e) {
            // 捕獲任何異常並印出信息
            e.printStackTrace();
        }
        // 當發生異常時，返回錯誤信息，表示內部伺服器錯誤
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
