package com.jensen.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 自訂例外類別：用來表示資源（如 Todo、使用者等）查無結果的情況。
 * 當這個例外被拋出時，Spring 會自動回傳 HTTP 404 Not Found 狀態碼。
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND) // 當此例外被觸發時，自動回傳 404 錯誤給前端
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 建構子：接收一個錯誤訊息，傳給父類別 RuntimeException
     *
     * @param message 錯誤訊息，例如 "Todo not found with id: 1"
     */
    public ResourceNotFoundException(String message) {
        super(message); // 呼叫父類別的建構子，將錯誤訊息保存
    }
}
