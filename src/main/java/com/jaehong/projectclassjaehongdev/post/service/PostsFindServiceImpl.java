package com.jaehong.projectclassjaehongdev.post.service;

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

    @Override
    public PostsResponse execute() {
        return PostsResponse.from(postRepository.findAll()
                .stream().map(post ->
                        PostFindResponse.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .build()
                ).collect(Collectors.toList()));
    }
}
