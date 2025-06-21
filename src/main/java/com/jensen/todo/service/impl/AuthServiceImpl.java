package com.jensen.todo.service.impl;

import com.jensen.todo.dto.RegisterDTO;
import com.jensen.todo.entity.Role;
import com.jensen.todo.entity.User;
import com.jensen.todo.exception.TodoAPIException;
import com.jensen.todo.repository.RoleRepository;
import com.jensen.todo.repository.UserRepository;
import com.jensen.todo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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


    @Override
    public String register(RegisterDTO registerDTO) {
        // check username is already exists in database
        if(userRepository.existsByUsername(registerDTO.getUsername())){
            throw new TodoAPIException("Username already exists!", HttpStatus.BAD_REQUEST);
        }

        // check email is already exists in database
        if(userRepository.existsByEmail(registerDTO.getEmail())){
            throw new TodoAPIException("Email is already exists!.", HttpStatus.BAD_REQUEST);
        }


        User user = new User();

        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Role roleUser = roleRepository.findByName("ROLE_USER");
        Set<Role> roles=new HashSet<>();
        roles.add(roleUser);
        user.setRoleSet(roles);

        userRepository.save(user);
        return "用戶註冊成功！";
    }
}
