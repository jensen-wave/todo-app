package com.jensen.todo.service;

import com.jensen.todo.dto.LoginDTO;
import com.jensen.todo.dto.RegisterDTO;

public interface AuthService {
    String register(RegisterDTO registerDTO);
    String login(LoginDTO loginDTO);
}
