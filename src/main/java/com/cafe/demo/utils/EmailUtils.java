package com.cafe.demo.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 郵件工具類，用於發送簡單郵件
 */
@Service
public class EmailUtils {

    // 自動注入 Spring 提供的 JavaMailSender，用於發送郵件
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 發送簡單郵件
     * 
     * @param to 收件人郵箱地址
     * @param subject 郵件主題
     * @param text 郵件正文內容
     * @param list 抄送地址列表（可選）
     */
    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
        // 創建 SimpleMailMessage 對象
        SimpleMailMessage message = new SimpleMailMessage();

        // 設置發件人郵箱地址（需要與 spring.mail.username 相同）
        message.setFrom("aaa111334@gmail.com");

        // 設置收件人
        message.setTo(to);

        // 設置郵件主題
        message.setSubject(subject);

        // 設置郵件正文
        message.setText(text);

        // 如果有抄送地址，設置抄送
        if (list != null && list.size() > 0) {
            message.setCc(getCcArray(list));
        }

        // 發送郵件
        javaMailSender.send(message);
    }

    /**
     * 將抄送地址列表轉換為陣列
     * 
     * @param ccList 抄送地址列表
     * @return 抄送地址的 String 陣列
     */
    private String[] getCcArray(List<String> ccList) {
        // 創建一個與列表大小相同的陣列
        String[] cc = new String[ccList.size()];

        // 使用迴圈將列表元素轉移到陣列中
        for (int i = 0; i < ccList.size(); i++) {
            cc[i] = ccList.get(i);
        }

        // 返回生成的陣列
        return cc;
    }

}
