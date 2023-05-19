package com.jaehong.projectclassjaehongdev.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.utils.DomainExceptionValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class 회원_도메인을 {
    @Nested
    class 생성할_때 {
        @Test
        void 이메일이_null인_경우_생성할_수_없습니다() {
            var domainException = DomainExceptionCode.MEMBER_SHOULD_NOT_EMAIL_EMPTY.create();
            assertThatThrownBy(() -> Member.create(null, "password"))
                    .isInstanceOf(DomainException.class)
                    .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));

        }

        @ParameterizedTest
        @ValueSource(ints = {1, 3, 10, 1000})
        void 이메일이_비어있는_경우_생성할_수_없습니다(int size) {
            var input = " ".repeat(size);
            var domainException = DomainExceptionCode.MEMBER_SHOULD_NOT_EMAIL_EMPTY.create();
            assertThatThrownBy(() -> Member.create(input, "password"))
                    .isInstanceOf(DomainException.class)
                    .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));
        }

        @ParameterizedTest
        @ValueSource(strings = {"1234", "email@", "  @"})
        void 이메일이_올바르지_않는_경우_에러가_발생합니다(String input) {
            var domainException = DomainExceptionCode.MEMBER_EMAIL_INVALID.create(Email.create(input).getValue());
            assertThatThrownBy(() -> Member.create(input, "password"))
                    .isInstanceOf(DomainException.class)
                    .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));
        }

        @ParameterizedTest
        @ValueSource(strings = {"email@email.com ", "e m a i l @ e m a i l . com"})
        void 이메일에_공백이_들어가더라도_제거된_상태로_생성됩니다(final String input) {
            var member = Member.create(input, "password");
            Assertions.assertThat(member.getEmail()).isEqualTo("email@email.com");
        }

        @Test
        void 패스워드에_공백혹은_null이_들어가면_생성할_수_없습니다() {
            var domainException = DomainExceptionCode.MEMBER_SHOULD_NOT_PASSWORD_EMPTY.create();
            assertAll(() -> assertThatThrownBy(() -> Member.create("email@email.com", null))
                            .satisfies((error) -> DomainExceptionValidator.validate(error, domainException)),
                    () -> assertThatThrownBy(() -> Member.create("email@email.com", ""))
                            .satisfies((error) -> DomainExceptionValidator.validate(error, domainException)));
        }

        @ParameterizedTest()
        @ValueSource(ints = {6, 7, 16, 17})
        void 이메일이_8자_이상_15자_이하가_아닌경우_오류가_발생합니다(int size) {
            var domainException = DomainExceptionCode.MEMBER_PASSWORD_INVALID_SIZE.create(8, 15, size);
            var password = "-".repeat(size);
            assertThatThrownBy(() -> Member.create("email@email.com", password))
                    .isInstanceOf(DomainException.class)
                    .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));
        }

        @ParameterizedTest
        @ValueSource(strings = {"password ", "p a s s w o r d"})
        void 패스워드에_공백이_들어가더라도_제거된_상태로_생성됩니다(final String input) {
            var member = Member.create("email@email.com", input);
            Assertions.assertThat(member.getPassword()).isEqualTo("password");
        }
    }

}