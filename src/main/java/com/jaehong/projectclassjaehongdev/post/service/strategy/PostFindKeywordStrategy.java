package com.jaehong.projectclassjaehongdev.post.service.strategy;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostsResponse;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import com.jaehong.projectclassjaehongdev.post.vo.PostSearchCondition;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("PostFindKeywordStrategy")
@RequiredArgsConstructor
public class PostFindKeywordStrategy extends PostSearchStrategy {
    private final PostRepository postRepository;

    @Override
    public PostsResponse findBy(PostSearch postSearch) {
        if (postSearch.getTitle().isBlank()) {
            throw DomainExceptionCode.POST_SEARCH_KEYWORD_SHOULD_NOT_BE_BLANK.generateError(postSearch.getTitle());
        }
        return PostsResponse.from(postRepository.findBy(PostSearchCondition.create(postSearch.getTitle()))
                .stream().map(this::convertEntityToResponse)
                .collect(Collectors.toList()));
    }
}
