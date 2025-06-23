package com.jensen.todo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


// 這是一個 JWT (JSON Web Token) 的工具類，負責生成、解析和驗證 Token。
@Component
public class JwtTokenProvider {

    // 使用 @Value 註解從 application.properties 設定檔中注入 JWT 密鑰。
    // 這個密鑰是用來進行簽名和驗證的，非常重要。
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    // 從 application.properties 設定檔中注入 JWT 的過期時間（以毫秒為單位）。
    @Value("${app.jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    /**
     * 根據已認證的 Authentication 物件生成一個 JWT Token。
     * @param authentication Spring Security 的認證物件，包含了已登入使用者的資訊。
     * @return 代表使用者的 JWT 字串。
     */
    public String generateToken(Authentication authentication) {
        // 從 Authentication 物件中獲取使用者名稱（或 email）。
        String username = authentication.getName();

        // 獲取當前時間，作為 Token 的起始時間。
        Date currentDate = new Date();

        // 計算 Token 的過期時間。
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        // 使用 JJWT 函式庫的 Builder 模式來建立 Token。
        String token = Jwts.builder()
                .setSubject(username) // 將使用者名稱設置為 Token 的 "主體" (Subject)。
                .setIssuedAt(new Date()) // 設置 Token 的 "發行時間" (Issued At)。
                .setExpiration(expireDate) // 設置 Token 的 "過期時間" (Expiration)。
                .signWith(key()) // 使用私有的 key() 方法生成的密鑰來進行簽名。
                .compact(); // 將所有設定組合起來，生成最終的 Token 字串。

        return token;
    }

    /**
     * 這是一個私有輔助方法，用於將 Base64 編碼的 JWT 密鑰字串轉換為 Key 物件。
     * @return 用於簽名和驗證的 Key 物件。
     */
    private Key key() {
        // 將 Base64 編碼的密鑰字串解碼，並使用 HMAC-SHA 演算法生成 Key 物件。
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    /**
     * 從傳入的 JWT Token 中解析出使用者名稱。
     * @param token 用戶端傳來的 JWT 字串。
     * @return Token 中包含的使用者名稱。
     */
    public String getUsername(String token) {
        // 使用 JJWT 函式庫的 Parser Builder 來解析 Token。
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key()) // 必須使用與生成時相同的密鑰來驗證簽名。
                .build()
                .parseClaimsJws(token) // 解析 Token 並驗證，如果驗證失敗會拋出例外。
                .getBody(); // 獲取 Token 的 "內容" (Payload)，也就是 Claims 物件。

        // 從 Claims 物件中獲取 "主體" (Subject)，也就是我們當初存入的使用者名稱。
        String username = claims.getSubject();

        return username;
    }

    /**
     * 驗證傳入的 JWT Token 是否有效（簽名是否正確、是否在有效期內等）。
     * @param token 要驗證的 JWT 字串。
     * @return 如果 Token 有效，返回 true。如果無效（例如簽名錯誤、過期），JJWT 會拋出例外。
     */
    public boolean validateToken(String token) {
        // 使用 parserBuilder 來解析並驗證 Token。
        Jwts.parserBuilder()
                .setSigningKey(key()) // 指定驗證用的密鑰。
                .build() // 構建解析器物件
                .parse(token); // 執行解析與驗證。如果 Token 無效，這行程式碼會拋出例外。

        // 如果程式能執行到這裡，表示 Token 驗證通過。
        return true;
    }
}