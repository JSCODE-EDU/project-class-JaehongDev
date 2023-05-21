package com.jaehong.projectclassjaehongdev.global.authentication.exception;


import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private static final String message = "유효하지 않는 인증입니다.";

    public AuthenticationException() {
        super(message);
    }
}
