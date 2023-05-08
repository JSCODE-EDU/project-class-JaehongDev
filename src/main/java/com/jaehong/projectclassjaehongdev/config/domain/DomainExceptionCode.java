package com.jaehong.projectclassjaehongdev.config.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DomainExceptionCode {
    POST(1000, "전체적인 에러 메시지 입니다."),
    POST_SHOULD_NOT_TITLE_EMPTY(POST.code + 1, "게시글 제목은 필수적으로 필요합니다."),
    POST_SHOULD_NOT_CONTENT_EMPTY(POST.code + 2, "게시글 내용은 필수적으로 필요합니다.");

    private static final String BASIC_ERROR_FORMAT = "[ERROR] [CODE:%d] [Message: %s]";
    private final Integer code;
    private final String message;


    public DomainException generateError() {
        return new DomainException(this);
    }

    public String getMessage() {
        return String.format(BASIC_ERROR_FORMAT, this.code, this.message);
    }
}
