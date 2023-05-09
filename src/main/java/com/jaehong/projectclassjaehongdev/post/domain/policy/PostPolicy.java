package com.jaehong.projectclassjaehongdev.post.domain.policy;

import com.jaehong.projectclassjaehongdev.config.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.post.domain.Post;

public class PostPolicy {
    private static final Integer MAX_TITLE_SIZE = 100;
    private static final Integer MAX_CONTENT_SIZE = 1000;

    public static void validatePostCreate(Post post) {
        validatePostTitle(post.getTitle());
        validatePostContent(post.getContent());
    }

    /**
     * @param content null x should not over than 1000 words 공백 불가능 띄어쓰기 포함
     */
    private static void validatePostContent(String content) {
        if (content == null || content.isEmpty() || content.isBlank()) {
            throw DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY.generateError();
        }
        var contentLength = content.length();
        if (MAX_TITLE_SIZE < contentLength) {
            throw DomainExceptionCode.POST_CONTENT_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE.generateError(MAX_CONTENT_SIZE, contentLength);
        }

    }

    /**
     * @param title null x should not over than 100 words 공백 불가능 띄어쓰기 포함
     */
    private static void validatePostTitle(String title) {
        if (title == null || title.isEmpty() || title.isBlank()) {
            throw DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY.generateError();
        }
        var titleLength = title.length();

        if (MAX_TITLE_SIZE < titleLength) {
            throw DomainExceptionCode.POST_TITLE_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE.generateError(MAX_TITLE_SIZE, titleLength);
        }
    }
}
