package com.jaehong.projectclassjaehongdev.post.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostSearchCondition {
    private final int limit;
    private final String keyword;


    private PostSearchCondition(String title, Integer limit) {
        this.keyword = title;
        this.limit = Math.max(100, limit);
    }


    public static PostSearchCondition create(String title, Integer limit) {
        return new PostSearchCondition(title, limit);
    }

    public static PostSearchCondition createEmptyCondition() {
        return new PostSearchCondition("", 100);
    }
}
