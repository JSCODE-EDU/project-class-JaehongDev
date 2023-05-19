package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostsResponse;
import com.jaehong.projectclassjaehongdev.post.service.strategy.PostSearchStrategy;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PostsFindServiceImpl implements PostsFindService {
    private final PostSearchStrategy postFindAllStrategy;
    private final PostSearchStrategy postFindKeywordStrategy;

    public PostsFindServiceImpl(
            @Qualifier("PostFindAllStrategy") PostSearchStrategy postFindAllStrategy,
            @Qualifier("PostFindKeywordStrategy") PostSearchStrategy postFindKeywordStrategy) {
        this.postFindAllStrategy = postFindAllStrategy;
        this.postFindKeywordStrategy = postFindKeywordStrategy;
    }

    @Transactional(readOnly = true)
    @Override
    public PostsResponse execute(PostSearch postSearch) {
        if (Objects.nonNull(postSearch.getTitle())) {
            return this.postFindKeywordStrategy.findBy(postSearch);
        }
        return this.postFindAllStrategy.findBy(postSearch);
    }
}
