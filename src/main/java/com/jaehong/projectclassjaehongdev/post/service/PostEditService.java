package com.jaehong.projectclassjaehongdev.post.service;


import com.jaehong.projectclassjaehongdev.post.payload.request.PostEditRequest;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostEditResponse;

public interface PostEditService {
    PostEditResponse execute(Long postId, Long memberId, PostEditRequest editRequest);
}
