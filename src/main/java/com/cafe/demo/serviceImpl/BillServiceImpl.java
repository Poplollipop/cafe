package com.cafe.demo.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.demo.JWT.JwtFilter;
import com.cafe.demo.POJO.Bill;
import com.cafe.demo.constents.CafeConstants;
import com.cafe.demo.dao.BillDao;
import com.cafe.demo.service.BillService;
import com.cafe.demo.utils.CafeUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service // 標註此類別為 Service 類別
public class BillServiceImpl implements BillService {

    @Autowired // 自動注入 JwtFilter 和 BillDao 類別
    JwtFilter jwtFilter;

    @Autowired
    BillDao billDao;

    // 實現 generateReport 方法，生成報告
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("inside generateReport"); // 記錄日誌，表示進入方法
        try {
            String fillName;
            // 驗證 requestMap 參數
            if (validateRequestMap(requestMap)) {
                // 如果參數中有 "isGenerated" 且值為 false，則取出 uid
                if (requestMap.containsKey("isGenerated") && !(Boolean) requestMap.get("isGenerated")) {
                    fillName = (String) requestMap.get("uid");
                } else {
                    fillName = CafeUtils.getUID(); // 生成新的 UID
                    requestMap.put("uid", fillName); // 將 UID 加入請求資料
                    insertBill(requestMap); // 插入帳單資料
                }
                // 準備資料以生成報告
                String data = "Customer Name: " + requestMap.get("name") + "\n" +
                        "Contact Number: " + requestMap.get("contactNumber") + "\n" +
                        "Email: " + requestMap.get("email") + "\n" +
                        "Payment Method: " + requestMap.get("paymentMethod");

                // 創建 PDF 文檔並設置路徑
                Document document = new Document();
                PdfWriter.getInstance(document,
                        new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fillName + ".pdf"));
                document.open();
                setRectangleInPdf(document); // 設置 PDF 的矩形邊框
                // 添加標題
                Paragraph par = new Paragraph("Coffee Order Management System", getFont("Header"));
                par.setAlignment(Element.ALIGN_CENTER);
                document.add(par);
                // 添加訂單資料
                Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
                document.add(paragraph);
                // 創建表格並填充產品資料
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table); // 添加表頭
                JSONArray jsonArray = CafeUtils.getArrayFromString((String) requestMap.get("productDetails"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    addRows(table, CafeUtils.getMapFromJson(jsonArray.getString(i))); // 添加行資料
                }
                document.add(table);

                // 添加頁腳
                Paragraph footer = new Paragraph("Total: " + requestMap.get("totalAmount") + "\n" +
                        "Thank you for visiting, we hope to meet again!", getFont("Data"));
                document.add(footer);
                document.close();
                // 返回生成報告的 UID
                return new ResponseEntity<>("{\"uid\":\"" + fillName + "\"}", HttpStatus.OK);
            }
            // 如果參數無效，返回錯誤訊息
            return CafeUtils.getResponseEntity("Requested data not found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace(); // 輸出錯誤堆疊訊息
        }
        // 如果發生其他錯誤，返回內部伺服器錯誤
        return CafeUtils.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 添加表格的每一行
    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows"); // 記錄日誌，表示進入方法
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell((Double.toString((Double) data.get("total"))));
    }

    // 添加表格的表頭
    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader"); // 記錄日誌，表示進入方法
        Stream.of("Product Name", "Category", "Quantity", "Price", "Total Amount").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBorderWidth(2); // 設置邊框寬度
            header.setPhrase(new Phrase(columnTitle)); // 設置表頭內容
            header.setBackgroundColor(BaseColor.PINK); // 設置背景顏色
            header.setHorizontalAlignment(Element.ALIGN_CENTER); // 設置水平對齊
            header.setVerticalAlignment(Element.ALIGN_CENTER); // 設置垂直對齊
            table.addCell(header);
        });
    }

    // 根據類型返回字體
    private Font getFont(String type) {
        log.info("Inside getFont"); // 記錄日誌，表示進入方法
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    // 設置 PDF 邊框矩形
    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside setRectangleInPdf"); // 記錄日誌，表示進入方法
        Rectangle rec = new Rectangle(577, 825, 18, 15);
        rec.enableBorderSide(1);
        rec.enableBorderSide(2);
        rec.enableBorderSide(4);
        rec.enableBorderSide(8);
        rec.setBorderColor(BaseColor.BLACK); // 設置邊框顏色
        rec.setBorderWidth(1); // 設置邊框寬度
        document.add(rec);
    }

    // 插入帳單資料到資料庫
    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUid((String) requestMap.get("uid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String) requestMap.get("totalAmount")));
            bill.setProductDetails((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser()); // 設置創建者
            billDao.save(bill); // 儲存帳單資料
        } catch (Exception e) {
            e.printStackTrace(); // 輸出錯誤堆疊訊息
        }
    }

    // 驗證請求參數是否完整
    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email")
                && requestMap.containsKey("paymentMethod") && requestMap.containsKey("productDetails")
                && requestMap.containsKey("totalAmount");
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list = new ArrayList<>();
        if (jwtFilter.isAdmin()) {
            list = billDao.getAllBills();
        } else {
            list = billDao.getBillsByUserName(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getBillsPdf(Map<String, Object> requestMap) {
        log.info("Inside getBillsPdf: requestMap{}" + requestMap);
        try {
            byte[] byteArray = new byte[0];
            if (!requestMap.containsKey("uid") && validateRequestMap(requestMap)) {
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            }
            String filePath = CafeConstants.STORE_LOCATION + "\\" + (String) requestMap.get("uid") + ".pdf";
            if (CafeUtils.isFileExist(filePath)) {
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            } else {
                requestMap.put("isGenerated", false);
                generateReport(requestMap);
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray(String filePath) throws Exception {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

}
