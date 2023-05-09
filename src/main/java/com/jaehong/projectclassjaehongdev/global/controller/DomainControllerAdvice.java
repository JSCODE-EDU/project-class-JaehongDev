package com.jaehong.projectclassjaehongdev.global.controller;

import com.jaehong.projectclassjaehongdev.config.domain.DomainException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DomainControllerAdvice {

    @ExceptionHandler({DomainException.class})
    public ResponseEntity<?> throwDomainExceptionResponse(DomainException exception) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder().code(exception.getCode())
                        .message(exception.getMessage())
                        .build()
                );
    }

    @Getter
    static class ErrorResponse {
        int code;
        String message;

        @Builder
        public ErrorResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
