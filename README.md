# Cafe Management System

## 專案簡介
一個基於 Spring Boot 和 Angular 的咖啡廳管理系統，提供產品管理、訂單處理和使用者管理功能。

## 技術
- **後端**: Java Spring Boot
- **前端**: Angular
- **數據庫**: MySQL

## 啟動指南
1. 安裝所需依賴：`mvn install`
2. 啟動後端：`mvn spring-boot:run`


## 文件結構
```
/src
├── main
│   └── java/com/cafe/demo
│       ├── constents
│       ├── dao
│       ├── JWT
│       ├── POJO
│       ├── rest
│       ├── restImpl
│       ├── service
│       ├── serviceImpl
│       ├── utils
│       └── wrapper
```

## 文件解釋
  1. constents （常量資料夾）作用： 存放全局常量，即經常使用、不會改變的靜態值(static)。
  2. dao （資料訪問層）作用： 存放與數據庫交互的接口，通常使用 Spring Data JPA 提供的 Repository 接口。
  3. JWT （JSON Web Token）作用： 與用戶認證相關的邏輯，主要用於生成、驗證和解析 JWT。
  4. POJO （簡單物件）作用： 簡單的 Java 類（Plain Old Java Object），用於存放數據結構或實體類（與資料庫表對應與Entity相似）。
  5. rest （REST 控制層）作用： 定義 RESTful API 的入口點，處理 HTTP 請求和響應。
  6. restImpl （REST 實作）作用：用於實現 REST 控制器中的邏輯，從而讓接口和處理邏輯分開。
  7. service （服務層接口）作用： 定義業務邏輯的接口，用於封裝具體邏輯並與其他層解耦。
  8. serviceImpl （服務層實作）作用： 提供 service 中接口的具體實作，執行核心邏輯。
  與數據訪問層（dao）交互，執行邏輯處理，返回結果給控制層。
  9. utils （工具類）作用： 存放通用的工具類，提供跨模塊重用的功能。
  10. wrapper （包裝類）作用：用於包裝數據模型或查詢結果，特別是需要自定義返回結構時。
  內容：用於 API 返回 DTO（數據傳輸對象），通常只包含需要暴露的字段。

    控制層：rest 和 restImpl
    服務層：service 和 serviceImpl
    數據層：dao 和 POJO
    輔助層：utils、JWT、constents 和 wrapper

  ## POJO 和 Entity差異說明
  特徵	POJO	Entity
  定義	普通的 Java 類，用於封裝數據	與數據庫表映射的類，通常用於持久化操作
  用途	用於簡單數據封裝和交換	用於數據庫映射，通過 ORM 框架操作數據
  框架依賴	無框架依賴	需要 JPA 或 Hibernate 等 ORM 框架
  註解	通常不使用註解	使用 @Entity, @Id, @Column 等註解
  特性	沒有特殊的數據庫映射規則	會映射到數據庫表並支持 CRUD 操作
