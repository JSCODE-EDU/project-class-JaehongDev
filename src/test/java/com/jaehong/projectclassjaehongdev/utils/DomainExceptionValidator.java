package com.jaehong.projectclassjaehongdev.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jaehong.projectclassjaehongdev.config.domain.DomainException;

public class DomainExceptionValidator {

    public static void validate(Throwable error, DomainException domainException) {
        var throwsException = (DomainException) error;
        assertAll(() -> assertThat(throwsException.getCode()).isEqualTo(domainException.getCode()),
                () -> assertThat(throwsException.getMessage()).isEqualTo(domainException.getMessage()));
    }
}
