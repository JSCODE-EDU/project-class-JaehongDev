package com.jaehong.projectclassjaehongdev.globa.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.utils.DomainExceptionValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("도메인 에러 테스트")
class DomainExceptionCodeTest {

    @Test
    void 정상적으로_도메인_에러가_생성됩니다() {
        var domainException = DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY.create();
        assertThatThrownBy(() -> {
            throw domainException;
        }).isInstanceOf(DomainException.class)
                .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));
    }

    @Test
    void 파라미터가_존재하는_에러도_정상적으로_생성됩니다() {
        var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS.create(1L);
        assertThatThrownBy(() -> {
            throw domainException;
        }).isInstanceOf(DomainException.class)
                .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));
    }


}