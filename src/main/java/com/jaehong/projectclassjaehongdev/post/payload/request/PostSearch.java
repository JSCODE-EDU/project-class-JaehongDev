package com.jaehong.projectclassjaehongdev.post.payload.request;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@EqualsAndHashCode
@Getter
public class PostSearch {
    // 게시글 요청 고정
    private final int limit = 100;
    private final String title;

    @Builder
    public PostSearch(String title) {
        this.title = title;
    }


}
