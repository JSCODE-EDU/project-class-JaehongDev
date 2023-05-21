package com.jaehong.projectclassjaehongdev.global.resolver;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.jaehong.projectclassjaehongdev.global.authentication.annotation.Secured;
import com.jaehong.projectclassjaehongdev.global.authentication.exception.AuthenticationException;
import com.jaehong.projectclassjaehongdev.jwt.TokenService;
import javax.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("ArgumentResolver의 토큰 검증 단위테스트")
class SignInArgumentResolverTest {
    @InjectMocks
    private SignInArgumentResolver signInArgumentResolver;
    @Mock
    private TokenService tokenService;
    @Mock
    private MethodParameter methodParameter;
    @Mock
    private ModelAndViewContainer modelAndViewContainer;
    @Mock
    private NativeWebRequest webRequest;
    @Mock
    private WebDataBinderFactory binderFactory;
    @Mock
    private HttpServletRequest httpRequest;

    @Test
    void Secured_애노테이션이_존재하지_않는_경우_에러가_발생한다() {
        given(methodParameter.getMethodAnnotation(Secured.class)).willReturn(null);
        assertThatThrownBy(() -> signInArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, webRequest, binderFactory))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void Authorization_헤더가_존재하지_않는경우_에러가_발생한다() {

        given(methodParameter.getMethodAnnotation(Secured.class)).willReturn(mock(Secured.class));
        given(webRequest.getNativeRequest()).willReturn(httpRequest);
        given(httpRequest.getHeader("Authorization")).willReturn(null);

        assertThatThrownBy(() -> signInArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, webRequest, binderFactory))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void 유효하지_않는_토큰의_경우_에러가_발생한다() {
        var token = "Bearer token";
        given(methodParameter.getMethodAnnotation(Secured.class)).willReturn(mock(Secured.class));
        given(webRequest.getNativeRequest()).willReturn(httpRequest);
        given(httpRequest.getHeader("Authorization")).willReturn(token);
        given(tokenService.verifyToken(any())).willReturn(false);

        assertThatThrownBy(() -> signInArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, webRequest, binderFactory))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void 정상적인_토큰의_경우_사용자_아이디를_반환한다() {
        var token = "Bearer token";
        given(methodParameter.getMethodAnnotation(Secured.class)).willReturn(mock(Secured.class));
        given(webRequest.getNativeRequest()).willReturn(httpRequest);
        given(httpRequest.getHeader("Authorization")).willReturn(token);
        given(tokenService.verifyToken(any())).willReturn(true);
        given(tokenService.getById(any())).willReturn(1L);

        var result = signInArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, webRequest, binderFactory);
        Assertions.assertThat(result).isEqualTo(1L);
    }

}