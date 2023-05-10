package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostFindResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostsResponse;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostsFindServiceImpl implements PostsFindService {
    private final PostRepository postRepository;

    private PostFindResponse convertEntityToResponse(Post post) {
        return PostFindResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }

    @Override
    public PostsResponse execute(PostSearch postSearch) {
        return PostsResponse.from(postRepository.findBy(postSearch)
                .stream().map(this::convertEntityToResponse)
                .collect(Collectors.toList()));
    }
}
