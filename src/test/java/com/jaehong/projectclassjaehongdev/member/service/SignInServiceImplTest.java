package com.jaehong.projectclassjaehongdev.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.jwt.TokenServiceImpl;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignInRequest;
import com.jaehong.projectclassjaehongdev.member.payload.response.SignInResponse;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.utils.DomainExceptionValidator;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class SignInServiceImplTest {
    @InjectMocks
    private SignInServiceImpl signInInService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TokenServiceImpl tokenService;

    /**
     *
     */
    @Test
    void 존재하지_않는_사용자_정보로_로그인하는_경우_에러가_발생합니다() {
        var domainException = DomainExceptionCode.AUTH_DID_NOT_CORRECT_LOGIN_INFORMATION.create();
        var request = SignInRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> signInInService.execute(request))
                .isInstanceOf(DomainException.class)
                .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));
    }

    @Test
    void 패스워드가_부정확하면_에러가_발생합니다() {
        var domainException = DomainExceptionCode.AUTH_DID_NOT_CORRECT_LOGIN_INFORMATION.create();
        var request = SignInRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();
        var member = Member.create("email@email.com", "otherpassword");
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        assertThatThrownBy(() -> signInInService.execute(request))
                .isInstanceOf(DomainException.class)
                .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));
    }

    @Test
    void 로그인이_성공적으로_진행되면_토큰이_발급됩니다() {
        var request = SignInRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();
        var response = SignInResponse.from("token");
        var member = Member.create("email@email.com", "password");
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        when(tokenService.issuedToken(any(), anyLong())).thenReturn(response.getToken());

        signInInService.execute(request);
        assertThat(response.getToken()).isEqualTo("token");
    }

}