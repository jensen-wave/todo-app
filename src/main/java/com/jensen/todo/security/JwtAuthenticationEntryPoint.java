package com.jensen.todo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 當使用者試圖訪問需要授權的資源但未經授權時，Spring Security 將會呼叫這個類別來處理未授權的請求。
 */
@Component  // 將這個類別註冊為 Spring 管理的 Bean
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * commence 方法會在使用者未通過身份驗證卻試圖存取保護資源時被呼叫。
     * 此處會回傳 401 Unauthorized 錯誤。
     *
     * @param request 當前的 HTTP 請求物件
     * @param response 當前的 HTTP 回應物件
     * @param authException 代表認證失敗的異常資訊
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 回傳 HTTP 狀態碼 401，並附上錯誤訊息
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
