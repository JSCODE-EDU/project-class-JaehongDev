package com.jaehong.projectclassjaehongdev.utils;

import static org.junit.jupiter.api.Assertions.assertAll;

import com.jaehong.projectclassjaehongdev.config.domain.DomainException;
import com.jaehong.projectclassjaehongdev.config.domain.DomainExceptionCode;
import org.assertj.core.api.Assertions;

public class DomainExceptionValidator {

    public static void validateDomainException(Throwable error, DomainExceptionCode domainExceptionCode) {
        var domainException = (DomainException) error;
        assertAll(() -> Assertions.assertThat(domainException.getCode()).isEqualTo(domainExceptionCode.getCode()));
    }
}
