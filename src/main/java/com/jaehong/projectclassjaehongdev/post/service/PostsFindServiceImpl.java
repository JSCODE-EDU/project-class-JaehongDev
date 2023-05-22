package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostsResponse;
import com.jaehong.projectclassjaehongdev.post.service.strategy.PostFindAllStrategy;
import com.jaehong.projectclassjaehongdev.post.service.strategy.PostFindKeywordStrategy;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class PostsFindServiceImpl implements PostsFindService {
    private final PostFindAllStrategy postFindAllStrategy;
    private final PostFindKeywordStrategy postFindKeywordStrategy;

    @Transactional(readOnly = true)
    @Override
    public PostsResponse execute(PostSearch postSearch) {
        if (Objects.nonNull(postSearch.getTitle())) {
            return this.postFindKeywordStrategy.findBy(postSearch);
        }
        return this.postFindAllStrategy.findBy(postSearch);
    }
}
