package com.jaehong.projectclassjaehongdev.post.domain;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jaehong.projectclassjaehongdev.config.domain.DomainException;
import com.jaehong.projectclassjaehongdev.config.domain.DomainExceptionCode;
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
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("게시글 생성")
    class PostCreate {
        @ParameterizedTest
        @ValueSource(strings = {"", "    "})
        void 제목이_없으면_오류가_발생한다(String input) {
            assertThatThrownBy(() -> {
                Post.createNewPost(input, "post");
            }).isInstanceOf(DomainException.class)
                    .satisfies(error -> {
                        var domainExceptionCode = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY;
                        var domainException = (DomainException) error;
                        assertAll(() -> Assertions.assertThat(domainException.getCode()).isEqualTo(domainExceptionCode.getCode()));
                    });
        }

        @Test
        void 제목이_null_인_경우_오류가_발생한다() {
            assertThatThrownBy(() -> {
                Post.createNewPost(null, "post");
            }).isInstanceOf(DomainException.class)
                    .satisfies(error -> {
                        var domainExceptionCode = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY;
                        var domainException = (DomainException) error;
                        assertAll(() -> Assertions.assertThat(domainException.getCode()).isEqualTo(domainExceptionCode.getCode()));
                    });
        }

        @ParameterizedTest
        @ValueSource(ints = {101, 102})
        void 제목이_100글자를_넘기면_오류가_발생한다(int size) {
            var input = "-".repeat(size); // 100글자가 넘는 문자열 생성
            assertThatThrownBy(() -> {
                Post.createNewPost(input, "post");
            }).isInstanceOf(DomainException.class)
                    .satisfies(error -> {
                        var domainExceptionCode = DomainExceptionCode.POST_TITLE_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE;
                        var domainException = (DomainException) error;
                        assertAll(() -> Assertions.assertThat(domainException.getCode()).isEqualTo(domainExceptionCode.getCode()));
                    });
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "    "})
        void 내용이_없으면_오류가_발생한다(String input) {
            assertThatThrownBy(() -> {
                Post.createNewPost("title", input);
            }).isInstanceOf(DomainException.class)
                    .satisfies(error -> {
                        var domainExceptionCode = DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY;
                        var domainException = (DomainException) error;
                        assertAll(() -> Assertions.assertThat(domainException.getCode()).isEqualTo(domainExceptionCode.getCode()));
                    });
        }

        @ParameterizedTest
        @ValueSource(ints = {1001, 1002})
        void 내용이_1000자가_넘으면_오류가_발생한다(int size) {
            var input = "-".repeat(size); // 1000글자가 넘는 문자열 생성
            assertThatThrownBy(() -> {
                Post.createNewPost("title", input);
            }).isInstanceOf(DomainException.class)
                    .satisfies(error -> {
                        var domainExceptionCode = DomainExceptionCode.POST_CONTENT_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE;
                        var domainException = (DomainException) error;
                        assertAll(() -> Assertions.assertThat(domainException.getCode()).isEqualTo(domainExceptionCode.getCode()));
                    });
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("게시글 삭제")
    class PostDelete {
        @Test
        void 테스트() {

        }
    }

}