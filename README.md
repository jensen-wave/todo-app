# 📝 Todo Management Backend API


## 📌 專案簡介

此專案為一個基於 Spring Boot 開發的 Todo 待辦事項管理系統，提供 RESTful API，並整合使用者註冊 / 登入（JWT 認證）、角色權限控制、任務 CRUD 管理功能。使用者可透過前端或 Postman 客戶端，安全地操作任務資料。

---

## 🔧 使用技術

* Java 17
* Spring Boot 3.x
* Spring Security 6（JWT 認證）
* Spring Data JPA
* H2 / MySQL（JPA 資料庫互動）
* ModelMapper（DTO/Entity 映射）
* Lombok（簡化 Java 類別撰寫）
* Postman（API 測試工具）

---

## 🔐 認證與授權

* 採用 **JWT（JSON Web Token）** 作為認證機制。
* 未授權存取會由 `JwtAuthenticationEntryPoint` 統一回傳 401 錯誤。
* 所有 `/api/todos/**` API 均需認證，並依角色進行授權。
* 兩種角色：

  * `ROLE_USER`：可查詢、標記完成/未完成
  * `ROLE_ADMIN`：可新增、修改、刪除任務

---

## ✅ 功能模組說明

### 使用者管理 API（`/api/auth`）

| Method | Endpoint    | 說明              | 權限 |
| ------ | ----------- | --------------- | -- |
| POST   | `/register` | 註冊新帳戶           | 公開 |
| POST   | `/login`    | 登入並取得 JWT Token | 公開 |

### Todo 管理 API（`/api/todos`）

| Method | Endpoint           | 說明     | 權限角色            |
| ------ | ------------------ | ------ | --------------- |
| POST   | `/`                | 建立待辦事項 | `ADMIN`         |
| GET    | `/{id}`            | 查詢指定事項 | `USER`, `ADMIN` |
| GET    | `/`                | 查詢所有事項 | `USER`, `ADMIN` |
| PUT    | `/{id}`            | 更新事項   | `ADMIN`         |
| DELETE | `/{id}`            | 刪除事項   | `ADMIN`         |
| PATCH  | `/{id}/complete`   | 標記為完成  | `USER`, `ADMIN` |
| PATCH  | `/{id}/incomplete` | 標記為未完成 | `USER`, `ADMIN` |

---

## 📁 專案結構概覽

```
src/main/java/com/jensen/todo
│
├── controller          # Auth & Todo REST Controller
├── dto                # 請求與回應的資料封裝
├── entity             # JPA Entity 類別 (User, Todo, Role)
├── exception          # 自定義錯誤處理與全域 Exception Handler
├── repository         # Spring Data JPA 介面
├── security           # JWT 認證邏輯與過濾器
├── service            # Auth & Todo 的 Service 介面與實作
├── config             # Spring Security 配置類
└── TodoManagementApplication.java
```

---

## 🚀 快速開始

### 1️⃣ clone 本專案

```bash
git clone https://github.com/your-username/todo-management-api.git
cd todo-management-api
```

### 2️⃣ 設定資料庫（可選）

修改 `application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todo_db
spring.datasource.username=root
spring.datasource.password=yourpassword
app.jwt.secret=your_secret_key_base64_encoded
app.jwt-expiration-milliseconds=604800000
```

或預設使用 `H2` 記憶體資料庫。

### 3️⃣ 啟動應用程式

```bash
./mvnw spring-boot:run
```

### 4️⃣ 測試 API

透過 [Postman](https://www.postman.com/) 測試以下流程：

1. `POST /api/auth/register`
2. `POST /api/auth/login` → 取得 token
3. 帶入 Bearer Token 後操作 `/api/todos` 相關 API

---

## 🔒 JWT 使用說明

Header:

```
Authorization: Bearer <your_token>
```

Token 內容包含：

* username
* 簽章時間與過期時間
* HS256 加密簽章

---

## 🧪 測試帳號（In-Memory）

若未接資料庫，可啟用 in-memory 帳號設定（預設註解在 `SpringSecurityConfig`）：

```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails admin = User.builder()
        .username("admin")
        .password(passwordEncoder().encode("123456"))
        .roles("ADMIN")
        .build();
    return new InMemoryUserDetailsManager(admin);
}
```

---

## 📄 License

本專案採用 MIT License，歡迎自由學習與使用。

---

是否需要幫你補上 API example requests 或自動化測試介紹？我也可以幫你生成 Swagger 文件配置。
