package com.jaehong.projectclassjaehongdev.config.domain;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final int code;

    public DomainException(DomainExceptionCode domainExceptionCode) {
        super(domainExceptionCode.getMessage());
        this.code = domainExceptionCode.getCode();
    }
}
