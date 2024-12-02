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
            " from Product p"
)


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
