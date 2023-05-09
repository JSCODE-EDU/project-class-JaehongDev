package com.jaehong.projectclassjaehongdev.post.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public class PostFindResponse {

    private final Long id;
    private final String title;
    private final String content;

}

