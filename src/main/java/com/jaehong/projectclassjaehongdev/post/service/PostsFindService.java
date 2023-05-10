package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostsResponse;

public interface PostsFindService {
    PostsResponse execute(PostSearch postSearch);
}
