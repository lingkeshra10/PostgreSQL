package com.postgresql.demo.exception;

import com.postgresql.demo.modal.ResponseModal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseModal> handleBadRequest(
            BadRequestException exception
    ) {
        ResponseModal response = new ResponseModal();
        response.setCode(exception.getCode());
        response.setMessage(exception.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
}