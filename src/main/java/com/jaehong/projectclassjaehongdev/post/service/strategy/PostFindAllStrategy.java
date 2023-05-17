package com.jaehong.projectclassjaehongdev.post.service.strategy;

import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostsResponse;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import com.jaehong.projectclassjaehongdev.post.vo.PostSearchCondition;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("PostFindAllStrategy")
public class PostFindAllStrategy extends PostSearchStrategy {
    private final PostRepository postRepository;

    @Override
    public PostsResponse findBy(PostSearch postSearch) {
        return PostsResponse.from(postRepository.findBy(PostSearchCondition.createEmptyCondition())
                .stream().map(this::convertEntityToResponse)
                .collect(Collectors.toList()));
    }
}
