package com.jaehong.projectclassjaehongdev.post.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.utils.DomainExceptionValidator;
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
    private final Member member = Member.create("email@email.com", "password");

    @Nested
    @DisplayName("게시글을 생성하는 경우")
    class PostCreate {
        @ParameterizedTest
        @ValueSource(strings = {"", "    "})
        void 제목이_없으면_오류가_발생한다(String input) {
            var domainException = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY.create(input);
            assertThatThrownBy(() -> Post.create(input, "post", member))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

        @Test
        void 제목이_null_인_경우_오류가_발생한다() {
            var domainException = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY.create(null);
            assertThatThrownBy(() -> Post.create(null, "post", member))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

        @ParameterizedTest
        @ValueSource(ints = {11, 12})
        void 제목이_100글자를_넘기면_오류가_발생한다(int size) {
            var domainException = DomainExceptionCode.POST_TITLE_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE.create(10, size);
            var input = "-".repeat(size); // 100글자가 넘는 문자열 생성
            assertThatThrownBy(() -> Post.create(input, "post", member))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "    "})
        void 내용이_없으면_오류가_발생한다(String input) {
            var domainException = DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY.create(input);
            assertThatThrownBy(() -> Post.create("title", input, member))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

        @Test
        void 내용이_null_인_경우_오류가_발생한다() {
            var domainException = DomainExceptionCode.POST_SHOULD_NOT_CONTENT_EMPTY.create(null);
            assertThatThrownBy(() -> Post.create("title", null, member))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

        @ParameterizedTest
        @ValueSource(ints = {1001, 1002})
        void 내용이_1000자가_넘으면_오류가_발생한다(int size) {
            var input = "-".repeat(size); // 1000글자가 넘는 문자열 생성
            var domainException = DomainExceptionCode.POST_CONTENT_SIZE_SHOULD_NOT_OVER_THAN_MAX_VALUE.create(1000, size);
            assertThatThrownBy(() -> Post.create("title", input, member))
                    .isInstanceOf(DomainException.class)
                    .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
        }

    }


    @Nested
    @DisplayName("게시글 수정을 하는 경우")
    class PostUpdate {
        private final String previousTitle = "title";
        private final String previousContent = "content";
        private final String nextTitle = "next-title";
        private final String nextContent = "next-content";

        @Test
        void 데이터가_변경됩니다() {

            var post = Post.create(previousTitle, previousContent, member);
            var updatePost = post.update(PostEditor.builder()
                    .title(nextTitle)
                    .content(nextContent)
                    .writer(member).build());

            assertAll(() -> assertThat(updatePost.getTitle()).isEqualTo(nextTitle),
                    () -> assertThat(updatePost.getContent()).isEqualTo(nextContent)
            );
        }

        @Test
        void 다른_작성자가_수정을_한다면_오류가_발생합니다() {

            var post = Post.create(previousTitle, previousContent, Member.create("other@email.com", "password"));
            var postEditor = PostEditor.builder()
                    .title(nextTitle)
                    .content(nextContent)
                    .writer(member).build();

            assertThatThrownBy(() -> post.update(postEditor))
                    .isInstanceOf(DomainException.class)
                    .satisfies((error) -> DomainExceptionValidator.validate(error, DomainExceptionCode.POST_INVALID_WRITER.create()));
        }
    }

}