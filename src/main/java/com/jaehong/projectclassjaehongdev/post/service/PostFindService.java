package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.post.payload.response.PostFindResponse;

public interface PostFindService {
    PostFindResponse execute(Long postId);
}
