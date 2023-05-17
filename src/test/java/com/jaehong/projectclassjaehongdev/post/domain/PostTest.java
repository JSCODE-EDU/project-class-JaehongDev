package com.jaehong.projectclassjaehongdev.post.domain;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.utils.DomainExceptionValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Post domain 테스트")
class PostTest {
    @Nested
    @DisplayName("게시글을 생성하는 경우")
    class PostCreate {
        @ParameterizedTest
        @ValueSource(strings = {"", "    "})
        void 제목이_없으면_오류가_발생한다(String input) {
            var domainException = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY.generateError(input);
            assertThatThrownBy(() -> Post.createNewPost(input, "post"))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

        @Test
        void 제목이_null_인_경우_오류가_발생한다() {
            var domainException = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY.generateError(null);
            assertThatThrownBy(() -> Post.createNewPost(null, "post"))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

        @ParameterizedTest
        @ValueSource(ints = {101, 102})
        void 제목이_100글자를_넘기면_오류가_발생한다(int size) {
            var domainException = DomainExceptionCode.POST_TITLE_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE.generateError(100, size);
            var input = "-".repeat(size); // 100글자가 넘는 문자열 생성
            assertThatThrownBy(() -> Post.createNewPost(input, "post"))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "    "})
        void 내용이_없으면_오류가_발생한다(String input) {
            var domainException = DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY.generateError(input);
            assertThatThrownBy(() -> Post.createNewPost("title", input))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

        @Test
        void 내용이_null_인_경우_오류가_발생한다() {
            var domainException = DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY.generateError(null);
            assertThatThrownBy(() -> Post.createNewPost("title", null))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

        @ParameterizedTest
        @ValueSource(ints = {1001, 1002})
        void 내용이_1000자가_넘으면_오류가_발생한다(int size) {
            var input = "-".repeat(size); // 1000글자가 넘는 문자열 생성
            var domainException = DomainExceptionCode.POST_CONTENT_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE.generateError(1000, size);
            assertThatThrownBy(() -> Post.createNewPost("title", input))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

    }


    @Nested
    @DisplayName("게시글 수정을 하는 경우")
    class PostUpdate {
        @Test
        void 데이터가_변경됩니다() {
            var previousTitle = "title";
            var previousContent = "content";
            var nextTitle = "next-title";
            var nextContent = "next-content";

            var post = Post.createNewPost(previousTitle, previousContent);
            var updatePost = post.updatePost(nextTitle, nextContent);

            assertAll(() -> Assertions.assertThat(updatePost.getTitle()).isEqualTo(nextTitle),
                    () -> Assertions.assertThat(updatePost.getContent()).isEqualTo(nextContent)
            );
        }
    }

}