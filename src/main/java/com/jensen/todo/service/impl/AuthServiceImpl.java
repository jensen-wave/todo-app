package com.jensen.todo.service.impl;

import com.jensen.todo.dto.LoginDTO;
import com.jensen.todo.dto.RegisterDTO;
import com.jensen.todo.entity.Role;
import com.jensen.todo.entity.User;
import com.jensen.todo.exception.TodoAPIException;
import com.jensen.todo.repository.RoleRepository;
import com.jensen.todo.repository.UserRepository;
import com.jensen.todo.security.JwtTokenProvider;
import com.jensen.todo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider; // 注入 JwtTokenProvider


    @Override
    public String register(RegisterDTO registerDTO) {
        // check username is already exists in database
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        // check email is already exists in database
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }


        User user = new User();

        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Role roleUser = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        user.setRoleSet(roles);

        userRepository.save(user);
        return "用戶註冊成功！";
    }

    @Override
    public String login(LoginDTO loginDTO) {

        // 1. 執行認證
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsernameOrEmail(),
                loginDTO.getPassword()
        ));

        // 2. 將認證資訊存入 SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 使用 JwtTokenProvider 生成 Token
        String token = jwtTokenProvider.generateToken(authentication);

        // 4. 返回生成的 Token
        return token;
    }
}
