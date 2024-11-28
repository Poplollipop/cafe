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

// 命名查詢，定義一個名為"User.findByEmailId"的查詢，通過email查找用戶
@NamedQuery(name = "User.findByEmailId", query = "select u from User u where u.email=:email")

@NamedQuery(name = "User.getAllUser", query = "select new package com.cafe.demo.wrapper.UserWrapper() from User u where u.role='user'")

@Data // Lombok註解，會自動生成getter, setter, toString, equals, hashCode等方法
@Entity // 標註該類為JPA實體類，對應到資料庫中的一張表
@DynamicUpdate // 表示只更新有變更的列，而不是更新整個實體
@DynamicInsert // 在插入時只插入非null字段，避免插入不必要的空值
@Table(name = "user") // 設定該實體類對應的資料庫表名為"user"

public class User implements Serializable { // User類實現Serializable接口，支持序列化

    private static final long serialVersionUID = 1L; // 設置serialVersionUID，這是序列化的版本控制ID

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

    public User() {

    }

}
