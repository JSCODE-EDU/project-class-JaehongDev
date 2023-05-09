package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.post.payload.request.PostCreateRequest;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostCreateResponse;

public interface PostCreateService {
    PostCreateResponse execute(PostCreateRequest request);
}
