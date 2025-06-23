package com.jensen.todo.config;

import com.jensen.todo.security.JwtAuthenticationEntryPoint;
import com.jensen.todo.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity  // 開啟方法層級的安全性註解，如 @PreAuthorize()
@Configuration         // 表示這是一個 Spring 設定類別
@AllArgsConstructor    // 自動為所有成員生成建構子（用於建構式注入）
public class SpringSecurityConfig {

    // 注入自定義的 UserDetailsService（Spring Security 將自動使用它）
    private UserDetailsService userDetailsService;

    // 注入自訂的認證入口點
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    // 注入自訂的認證過濾器
    private JwtAuthenticationFilter authenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()  // 關閉 CSRF 保護（REST API 通常不需要）
                .authorizeHttpRequests((authorize) -> {
                    // ↓ 可在這裡設定不同 HTTP 方法與路徑對應的權限（註解的是範例）
                    // authorize.requestMatchers(HttpMethod.POST,"/api/**").hasRole("ADMIN");
                    // authorize.requestMatchers(HttpMethod.PUT,"/api/**").hasRole("ADMIN");
                    // authorize.requestMatchers(HttpMethod.DELETE,"/api/**").hasRole("ADMIN");
                    // authorize.requestMatchers(HttpMethod.GET,"/api/**").hasAnyRole("ADMIN","USER");
                    // authorize.requestMatchers(HttpMethod.PATCH,"/api/**").hasAnyRole("ADMIN","USER");
                    // authorize.requestMatchers(HttpMethod.GET,"/api/**").permitAll();
                    authorize.requestMatchers("/api/auth/**").permitAll();
                    authorize.anyRequest().authenticated(); // 所有請求都需要驗證
                })
                .httpBasic(Customizer.withDefaults()); // 使用 HTTP Basic 認證（帳密輸入在 header 中）

        // 設定異常處理，特別是針對認證入口點
        http.exceptionHandling( exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint));

        // 將我們的 JWT 過濾器加到 Spring Security 的過濾器鏈中
        // 並且指定它要在 UsernamePasswordAuthenticationFilter 之前執行
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();  // 建立並返回安全性過濾器鏈
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 使用 BCrypt 加密密碼（業界標準）
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // 從 Spring 配置中取得 AuthenticationManager 實例
    }

    // 以下是測試用的 in-memory 帳號設定，當資料庫驗證尚未整合時可使用
    // 註解掉避免與自定義 UserDetailsService 衝突

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails ramesh = User.builder()
//                .username("Ramesh")
//                .password(passwordEncoder().encode("aaa"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("aaa"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(ramesh, admin); // 使用記憶體方式管理使用者帳號
//    }

}
