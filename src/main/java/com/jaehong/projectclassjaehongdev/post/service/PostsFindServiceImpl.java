package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostFindResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostsResponse;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import com.jaehong.projectclassjaehongdev.post.vo.PostSearchCondition;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostsFindServiceImpl implements PostsFindService {
    private final PostRepository postRepository;


    @Transactional(readOnly = true)
    @Override
    public PostsResponse execute(PostSearch postSearch) {
        if (Objects.nonNull(postSearch.getTitle())) {
            if (postSearch.getTitle().isBlank()) {
                throw DomainExceptionCode.POST_SEARCH_KEYWORD_SHOULD_NOT_BE_BLANK.generateError(postSearch.getTitle());
            }
            return this.findBy(PostSearchCondition.create(postSearch.getTitle()));
        }
        return this.findBy(PostSearchCondition.createEmptyCondition());
    }

    private PostsResponse findBy(PostSearchCondition postSearchCondition) {
        return PostsResponse.from(this.postRepository.findBy(postSearchCondition).stream()
                .map(this::convertEntityToResponse)
                .collect(Collectors.toList()));
    }

    private PostFindResponse convertEntityToResponse(Post post) {
        return PostFindResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }

    // 전략을 사용하는 방법
    // postSearch 를 바탕으로 검색 로직을 수행하는 인터페이스를 수행하는 방법으로 가야할듯
    // vo 형태로 
}
