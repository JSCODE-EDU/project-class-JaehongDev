package com.jaehong.projectclassjaehongdev.global.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public enum DomainExceptionCode {
    POST(1000, ""),
    POST_SHOULD_NOT_TITLE_EMPTY(POST.code + 1, "게시글 제목은 필수적으로 필요합니다."),
    POST_SHOULD_NOT_CONTENT_EMPTY(POST.code + 2, "게시글 내용은 필수적으로 필요합니다."),
    POST_TITLE_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE(POST.code + 3, "게시글 제목은 %d보다 길게 작성할 수 없습니다 (size: %d)"),
    POST_CONTENT_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE(POST.code + 4, "게시글 내용은 %d보다 길게 작성할 수 없습니다 (size: %d)"),
    POST_DID_NOT_EXISTS(POST.code + 5, "존재하지 않는 게시글 id 입니다 [id: %d]"),
    POST_SEARCH_KEYWORD_SHOULD_NOT_BE_BLANK(POST.code + 6, "게시글 키워드는 공백을 제외한 1글자 이상이여야 한다 (input: %s)"),
    POST_INVALID_WRITER(POST.code + 7, "게시글 작성자와 수정하는 사람의 id가 일치하지 않습니다."),

    MEMBER(2000, ""),
    MEMBER_SHOULD_NOT_EMAIL_EMPTY(MEMBER.code + 1, "회원의 이메일은 필수적으로 필요합니다."),
    MEMBER_EMAIL_INVALID(MEMBER.code + 2, "회원의 이메일이 유효하지 않습니다. (input: %s)"),
    MEMBER_SHOULD_NOT_PASSWORD_EMPTY(MEMBER.code + 3, "회원의 패스워드는 필수적으로 필요합니다."),
    MEMBER_PASSWORD_INVALID_SIZE(MEMBER.code + 4, "회원의 비밀번호의 길이는 %d이상 %d이하 입니다. (size: %d)"),
    MEMBER_EXISTS_EMAIL(MEMBER.code + 5, "이미 존재하는 이메일 입니다. (input: %s)"),
    MEMBER_ID_DID_NOT_EXISTS(MEMBER.code + 6, "존재하지 않는 회원 아이디 입니다 (input:%d)"),

    AUTH(3000, ""),
    AUTH_DID_NOT_CORRECT_LOGIN_INFORMATION(AUTH.code + 1, "부정확한 아이디 비밀번호 입니다.");

    private static final String BASIC_ERROR_FORMAT = "[ERROR] [CODE:%d] [Message: %s]";
    private final Integer code;
    private final String message;


    public DomainException create() {
        return new DomainException(code, this.generateErrorMessage());
    }

    private String generateErrorMessage() {
        return String.format(BASIC_ERROR_FORMAT, this.code, this.message);
    }

    public DomainException create(Object... args) {
        return new DomainException(code, this.generateErrorMessage(args));
    }

    private String generateErrorMessage(Object... args) {
        return String.format(BASIC_ERROR_FORMAT, this.code, String.format(this.message, args));
    }


    public String getMessage() {
        return String.format(BASIC_ERROR_FORMAT, this.code, this.message);
    }
}
