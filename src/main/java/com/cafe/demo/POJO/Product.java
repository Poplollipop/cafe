package com.cafe.demo.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(
        // 定義查詢名稱，方便在代碼中直接使用
        name = "Product.getAllProduct",
        // JPQL 查詢語句，返回一個 DTO 類型（ProductWrapper）的結果
        query = "select new com.cafe.demo.wrapper.ProductWrapper(" +
        // 選取的字段：Product 實體的屬性和關聯屬性
                " p.id, p.name, p.description, p.price, p.status, " +
                " p.category.id, p.category.name" +
                " ) " +
                // 查詢的實體來源
                " from Product p")

@NamedQuery(name = "Product.updateProductStatus", query = "update Product p set p.status=:status where p.id= :id")
/**
 * 更新產品狀態的查詢
 * 
 * 這個查詢根據產品ID更新產品的狀態。參數`status`為新狀態，
 * `id`為產品的ID。
 */
@NamedQuery(name = "Product.getProductByCategory", query = "select new com.cafe.demo.wrapper.ProductWrapper(p.id,p.name) from Product p where p.category.id=:id and p.status='true'")
/**
 * 根據類別ID取得產品的簡要資訊（ID與名稱）
 * 
 * 這個查詢根據產品的類別ID（`category.id`）和狀態為`'true'`來篩選
 * 產品，並返回產品的ID和名稱，封裝在`ProductWrapper`中。
 */
@NamedQuery(name = "Product.getProductById", query = "select new com.cafe.demo.wrapper.ProductWrapper(p.id,p.name,p.description,p.price) from Product p where p.id=:id")
/**
 * 根據產品ID取得詳細的產品資訊
 * 
 * 這個查詢根據產品的ID篩選產品，並返回產品的ID、名稱、描述和價格，
 * 封裝在`ProductWrapper`中。
 */

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tables")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    // 多對一關聯，表示該實體類型與 Category 類型之間存在多對一的關係
    // 使用 LAZY 加載策略，只有在需要訪問該屬性時才會查詢 Category 物件
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_kd", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status")
    private String status;
}
