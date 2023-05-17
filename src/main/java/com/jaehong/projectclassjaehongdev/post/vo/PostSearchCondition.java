package com.jaehong.projectclassjaehongdev.post.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostSearchCondition {
    private final int limit;
    private final String keyword;

    private PostSearchCondition(String keyword) {
        this.limit = 100;
        this.keyword = keyword;
    }

    public static PostSearchCondition create(String title) {
        return new PostSearchCondition(title);
    }

    public static PostSearchCondition createEmptyCondition() {
        return new PostSearchCondition("");
    }
}
