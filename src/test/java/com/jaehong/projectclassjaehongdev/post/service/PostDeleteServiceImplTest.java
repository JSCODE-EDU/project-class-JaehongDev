package com.jaehong.projectclassjaehongdev.post.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import com.jaehong.projectclassjaehongdev.utils.DomainExceptionValidator;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Post service 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class PostDeleteServiceImplTest {

    @InjectMocks
    private PostDeleteServiceImpl postDeleteService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostRepository postRepository;

    @Test
    void 게시글이_존재하지_않는_경우_에러를_발생시킵니다() {
        var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS.create(1L);
        final var writer = Member.create("writer@email.com", "password");

        given(memberRepository.findById(1L)).willReturn(Optional.of(writer));
        given(postRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> postDeleteService.execute(1L, 1L))
                .isInstanceOf(DomainException.class)
                .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
    }

    @Test
    void 사용자_아이디가_존재하지_않는_경우_에러를_발생시킵니다() {
        final var domainException = DomainExceptionCode.MEMBER_ID_DID_NOT_EXISTS.create(1L);

        given(memberRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> postDeleteService.execute(1L, 1L))
                .isInstanceOf(DomainException.class)
                .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
    }

    @Test
    void 게시글_작성자와_사용자_아이디가_다른_경우_에러를_발생시킵니다() {
        final var domainException = DomainExceptionCode.POST_INVALID_WRITER.create();
        final var writer = Member.create("writer@email.com", "password");
        final var otherMember = Member.create("other@email.com", "password");
        given(memberRepository.findById(1L)).willReturn(Optional.of(otherMember));
        given(postRepository.findById(1L)).willReturn(Optional.of(Post.create("title", "content", writer)));

        assertThatThrownBy(() -> postDeleteService.execute(1L, 1L))
                .isInstanceOf(DomainException.class)
                .satisfies(error -> DomainExceptionValidator.validate(error, domainException));


    }
}