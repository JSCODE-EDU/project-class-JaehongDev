package com.jaehong.projectclassjaehongdev.post.payload.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public class PostCreateResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
}
