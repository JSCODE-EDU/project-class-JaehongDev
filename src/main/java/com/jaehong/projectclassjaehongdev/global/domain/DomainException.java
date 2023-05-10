package com.jaehong.projectclassjaehongdev.global.domain;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final int code;

    public DomainException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
