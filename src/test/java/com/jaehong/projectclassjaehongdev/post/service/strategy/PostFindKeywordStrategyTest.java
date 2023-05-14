package com.jaehong.projectclassjaehongdev.post.service.strategy;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.utils.DomainExceptionValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class PostFindKeywordStrategyTest {
    @InjectMocks
    private PostFindKeywordStrategy postFindKeywordStrategy;

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 1000})
    void 키워드_검색_전략에서_게시글을_조회할때_키워드가_공백인_경우_에러를_발생시킨다(int size) {

        String input = " ".repeat(size);

        final var domainException = DomainExceptionCode.POST_SEARCH_KEYWORD_SHOULD_NOT_BE_BLANK.generateError(input);
        Assertions.assertThatThrownBy(() -> {
                    postFindKeywordStrategy.findBy(PostSearch.builder().title(input).build());
                }).isInstanceOf(DomainException.class)
                .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));
    }

}