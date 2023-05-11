package com.jaehong.projectclassjaehongdev.post.repository;

import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findBy(PostSearch pageable);
}
