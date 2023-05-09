package com.jaehong.projectclassjaehongdev.post.payload.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostsResponse {
    private final List<PostFindResponse> posts;

    public static PostsResponse from(List<PostFindResponse> posts) {
        return new PostsResponse(posts);
    }
}

