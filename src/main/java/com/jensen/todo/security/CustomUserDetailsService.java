package com.jensen.todo.security;

import com.jensen.todo.entity.User;
import com.jensen.todo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service  // 告訴 Spring 這是一個 Service 類別，會被作為 Bean 管理
@AllArgsConstructor  // 自動產生建構子，用來注入 userRepository
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;  // 注入 User 資料存取層，從資料庫讀取使用者資訊

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // 從資料庫依照「帳號或 Email」查詢使用者，若找不到則拋出例外
        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not exist by username or email")
                );

        // 將 User 實體中的角色集合 (Set<Role>) 轉為 Spring Security 可識別的 GrantedAuthority 集合
        Set<GrantedAuthority> authorities = user.getRoleSet()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))  // 將每個角色名稱轉為權限對象
                .collect(Collectors.toSet());

        // 建立並回傳 Spring Security 專用的 UserDetails 實例
        // 傳入參數為帳號（可為 username 或 email）、加密後密碼、角色權限集合
        return new org.springframework.security.core.userdetails.User(
                usernameOrEmail,  // 登入識別（username 或 email）
                user.getPassword(),  // 加密後密碼
                authorities  // 權限（角色）集合
        );
    }
}
