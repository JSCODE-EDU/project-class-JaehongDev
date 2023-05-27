package com.jaehong.projectclassjaehongdev.post.payload.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostFindResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    private final List<PostComment> comments;

    @Builder
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class PostComment {
        private final Long id;
        private final String content;
        private final LocalDateTime createdAt;
    }

}



