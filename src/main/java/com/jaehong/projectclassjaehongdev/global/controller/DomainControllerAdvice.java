package com.jaehong.projectclassjaehongdev.global.controller;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DomainControllerAdvice {

    @ExceptionHandler({DomainException.class})
    public ResponseEntity<ErrorResponse> throwDomainExceptionResponse(DomainException exception) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .code(exception.getCode())
                        .message(exception.getMessage())
                        .build()
                );
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponse> throwRuntimeException(RuntimeException exception) {
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.builder()
                        .code(1)
                        .message("서버에서 알 수 없는 에러가 발생했습니다.")
                        .build());

    }

    @Getter
    static class ErrorResponse {
        int code;
        String message;

        @Builder
        private ErrorResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
