package com.jaehong.projectclassjaehongdev.post.domain.policy;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.post.domain.Post;
import java.util.Objects;

public class PostValidationPolicy {
    private static final Integer MAX_TITLE_SIZE = 10;
    private static final Integer MAX_CONTENT_SIZE = 1000;

    public static void validateAll(Post post) {
        validatePostTitle(post.getTitle());
        validatePostContent(post.getContent());
    }

    /**
     * @param content null x should not over than 1000 words 공백 불가능 띄어쓰기 포함
     */
    private static void validatePostContent(String content) {
        if (content == null || content.isEmpty() || content.isBlank()) {
            throw DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY.create();
        }
        var contentLength = content.length();
        if (MAX_CONTENT_SIZE < contentLength) {
            throw DomainExceptionCode.POST_CONTENT_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE.create(MAX_CONTENT_SIZE, contentLength);
        }

    }

    /**
     * @param title null x should not over than 10 words 공백 불가능 띄어쓰기 포함
     */
    private static void validatePostTitle(String title) {
        if (title == null || title.isEmpty() || title.isBlank()) {
            throw DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY.create();
        }
        var titleLength = title.length();

        if (MAX_TITLE_SIZE < titleLength) {
            throw DomainExceptionCode.POST_TITLE_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE.create(MAX_TITLE_SIZE, titleLength);
        }
    }


    public static void validateWriter(Post post, Member writer) {
        if (!Objects.equals(post.getWriter(), writer)) {
            throw DomainExceptionCode.POST_INVALID_WRITER.create();
        }

    }
}
