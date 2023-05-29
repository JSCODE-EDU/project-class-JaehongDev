package com.jaehong.projectclassjaehongdev.post.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostEditRequest;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import com.jaehong.projectclassjaehongdev.utils.DomainExceptionValidator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("게시글 수정 service")
@ExtendWith(MockitoExtension.class)
class PostEditServiceImplTest {

    @InjectMocks
    private PostEditServiceImpl postEditService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private MemberRepository memberRepository;

    private PostEditRequest request;

    @BeforeEach
    void setup() {
        this.request = PostEditRequest.builder()
                .title("title")
                .content("content")
                .build();
    }

    @Test
    void 게시글이_존재하지_않는_경우_에러를_발생시킵니다() {
        final var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS.create(1L);

        given(memberRepository.findById(any())).willReturn(Optional.of(Member.create("email@email.com", "password")));
        given(postRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postEditService.execute(1L, 1L, request))
                .isInstanceOf(DomainException.class)
                .satisfies(error -> DomainExceptionValidator.validate(error, domainException));
    }

    @Test
    void 사용자_아이디가_존재하지_않는_경우_에러를_발생시킵니다() {
        final var domainException = DomainExceptionCode.MEMBER_ID_DID_NOT_EXISTS.create(1L);

        given(memberRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> postEditService.execute(1L, 1L, request))
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

        assertThatThrownBy(() -> postEditService.execute(1L, 1L, request))
                .isInstanceOf(DomainException.class)
                .satisfies(error -> DomainExceptionValidator.validate(error, domainException));


    }
}