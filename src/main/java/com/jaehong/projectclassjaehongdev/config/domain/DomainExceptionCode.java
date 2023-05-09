package com.jaehong.projectclassjaehongdev.config.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public enum DomainExceptionCode {
    POST(1000, "전체적인 에러 메시지 입니다."),
    POST_SHOULD_NOT_TITLE_EMPTY(POST.code + 1, "게시글 제목은 필수적으로 필요합니다."),
    POST_SHOULD_NOT_CONTENT_EMPTY(POST.code + 2, "게시글 내용은 필수적으로 필요합니다."),
    POST_TITLE_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE(POST.code + 3, "게시글 제목은 %d보다 길게 작성할 수 없습니다 (size: %d)"),
    POST_CONTENT_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE(POST.code + 4, "게시글 내용은 %d보다 길게 작성할 수 없습니다 (size: %d)");

    private static final String BASIC_ERROR_FORMAT = "[ERROR] [CODE:%d] [Message: %s]";
    private final Integer code;
    private final String message;


    public DomainException generateError() {
        return new DomainException(code, this.generateErrorMessage());
    }

    private String generateErrorMessage() {
        return String.format(BASIC_ERROR_FORMAT, this.code, this.message);
    }

    public DomainException generateError(Object... args) {
        return new DomainException(code, this.generateErrorMessage(args));
    }

    private String generateErrorMessage(Object... args) {
        return String.format(BASIC_ERROR_FORMAT, this.code, String.format(this.message, args));
    }


    public String getMessage() {
        return String.format(BASIC_ERROR_FORMAT, this.code, this.message);
    }
}
