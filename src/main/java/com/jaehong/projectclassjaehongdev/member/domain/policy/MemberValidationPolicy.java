package com.jaehong.projectclassjaehongdev.member.domain.policy;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import java.util.Objects;
import java.util.regex.Pattern;

public class MemberValidationPolicy {

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 15;

    public static void validate(Member member) {

        validateEmail(member.getEmail());
        validatePassword(member.getPassword());
    }

    /**
     * <p>
     * - `패스워드`에 공백이 포함될 수 없다. <br> - `패스워드`는 8자 이상 15자 이하여야 한다.
     * </p>
     *
     * @param password
     */
    private static void validatePassword(String password) {
        if (Objects.isNull(password)) {
            throw DomainExceptionCode.MEMBER_SHOULD_NOT_PASSWORD_EMPTY.create();
        }
        if (password.isBlank()) {
            throw DomainExceptionCode.MEMBER_SHOULD_NOT_PASSWORD_EMPTY.create();
        }
        var size = password.length();

        if (size < PASSWORD_MIN_LENGTH || size > PASSWORD_MAX_LENGTH) {
            throw DomainExceptionCode.MEMBER_PASSWORD_INVALID_SIZE.create(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH, size);
        }
    }

    /**
     * - `이메일`에 반드시 `@`가 1개만 포함되어 있어야 한다. <br> - `이메일`에 공백이 포함될 수 없다.
     */
    private static void validateEmail(String email) {
        if (Objects.isNull(email)) {
            throw DomainExceptionCode.MEMBER_SHOULD_NOT_EMAIL_EMPTY.create();
        }

        if (email.isBlank()) {
            throw DomainExceptionCode.MEMBER_SHOULD_NOT_EMAIL_EMPTY.create();
        }
        // 이메일 유효성 검사
        // email@.com 5322
        var emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        if (!emailPattern.matcher(email).matches()) {
            throw DomainExceptionCode.MEMBER_EMAIL_INVALID.create(email);
        }
    }
}
