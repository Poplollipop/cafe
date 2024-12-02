package com.cafe.demo.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

// 定義命名查詢，查詢所有的 Category 實體
@NamedQuery(name="Category.getAllCategory", query = "select c from Category c where c.id in (select p.category.id from Product p where p.status = 'true')")

@Data
@Entity
// 動態更新，表示在更新資料時只會更新實際改變的欄位
@DynamicUpdate
@DynamicInsert
@Table(name = "category")
// 序列化
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

}
