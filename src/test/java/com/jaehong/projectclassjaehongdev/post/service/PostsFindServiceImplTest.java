package com.jaehong.projectclassjaehongdev.post.service;

import static org.mockito.Mockito.mock;

import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.service.strategy.PostFindAllStrategy;
import com.jaehong.projectclassjaehongdev.post.service.strategy.PostFindKeywordStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class PostsFindServiceImplTest {

    private PostsFindServiceImpl postsFindService;
    private PostFindAllStrategy postFindAllStrategy;
    private PostFindKeywordStrategy postFindKeywordStrategy;


    @BeforeEach
    void setUp() {
        this.postFindAllStrategy = mock(PostFindAllStrategy.class);
        this.postFindKeywordStrategy = mock(PostFindKeywordStrategy.class);
        this.postsFindService = new PostsFindServiceImpl(postFindAllStrategy, postFindKeywordStrategy);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 1000})
    void 전략을_실행시키는_서비스에서_게시글을_조회할때_키워드가_공백인_경우_키워드_검색을_실행시킨다(int size) {

        String input = " ".repeat(size);

        var postSearch = PostSearch.builder().title(input).build();
        postsFindService.execute(postSearch);

        BDDMockito.verify(postFindKeywordStrategy).findBy(postSearch);
    }

    @ParameterizedTest
    @ValueSource(strings = {"title", "keyword", "jscode"})
    void 키워드가_공백이_아닌경우_키워드_검색을_실행시킨다(String input) {
        var postSearch = PostSearch.builder().title(input).build();
        postsFindService.execute(postSearch);
        BDDMockito.verify(postFindKeywordStrategy).findBy(postSearch);
    }

    @Test
    void 전략을_실행시키는_서비스에서_게시글이_null인경우_전체조회를_실행시킨다() {
        var postSearch = PostSearch.builder().build();
        postsFindService.execute(postSearch);
        BDDMockito.verify(postFindAllStrategy).findBy(postSearch);
    }
}