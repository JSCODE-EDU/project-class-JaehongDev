package com.jaehong.projectclassjaehongdev.post.service.strategy;

import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostFindResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostsResponse;

public abstract class PostSearchStrategy {
    abstract public PostsResponse findBy(PostSearch postSearch);

    protected PostFindResponse convertEntityToResponse(Post post) {
        return PostFindResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
