package com.jaehong.projectclassjaehongdev.post.payload.request;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class PostSearch {
    // 게시글 요청 고정
    private Integer limit;
    private String title;

    @Builder
    public PostSearch(String title, Integer limit) {
        this.title = title;
        this.limit = limit;
    }
}
