package com.jaehong.projectclassjaehongdev.post.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.jaehong.projectclassjaehongdev.config.domain.DomainException;
import com.jaehong.projectclassjaehongdev.config.domain.DomainExceptionCode;
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
    private PostRepository postRepository;

    @Test
    void 게시글이_존재하지_않는_경우_에러를_발생시킵니다() {
        given(postRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> postDeleteService.execute(1L))
                .isInstanceOf(DomainException.class)
                .satisfies(error -> DomainExceptionValidator.validateDomainException(error, DomainExceptionCode.POST_DID_NOT_EXISTS));
    }

}