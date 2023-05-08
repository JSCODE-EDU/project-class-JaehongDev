package com.jaehong.projectclassjaehongdev.config.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("도메인 에러 테스트")
class DomainExceptionCodeTest {

    @Test
    void 정상적으로_도메인_에러가_생성됩니다() {
        assertThatThrownBy(() -> {
            throw DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY.generateError();
        }).isInstanceOf(DomainException.class)
                .satisfies((error) -> {
                    var domainException = (DomainException) error;
                    assertAll(() -> assertThat(domainException.getCode()).isEqualTo(DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY.getCode()),
                            () -> assertThat(domainException.getMessage()).isEqualTo(DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY.getMessage())
                    );
                });
    }


}