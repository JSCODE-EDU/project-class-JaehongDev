package com.jaehong.projectclassjaehongdev.global.resolver;

import com.jaehong.projectclassjaehongdev.global.authentication.annotation.MemberId;
import com.jaehong.projectclassjaehongdev.global.authentication.annotation.Secured;
import com.jaehong.projectclassjaehongdev.global.authentication.exception.AuthenticationException;
import com.jaehong.projectclassjaehongdev.jwt.TokenService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Slf4j
@RequiredArgsConstructor
@Component
public class SignInArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_METHOD = "Bearer";
    private final TokenService tokenService;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class)
                && parameter.hasParameterAnnotation(MemberId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        validateInvalidMethodAnnotation(parameter);

        final var request = (HttpServletRequest) webRequest.getNativeRequest();
        final var authorizationHeader = request.getHeader(AUTHORIZATION);

        validateTokenAuthorizationHeader(authorizationHeader);

        final var token = authorizationHeader.substring(AUTHORIZATION_METHOD.length() + 1);
        validateInvalidJwtToken(parameter, token);
        return this.convertTokenToClaimsId(parameter, token);
    }

    private void validateInvalidMethodAnnotation(MethodParameter parameter) {
        if (parameter.getMethodAnnotation(Secured.class) == null) {
            log.error("@Secured 애노테이션을 붙혀주세요.");
            throw new AuthenticationException();
        }
    }

    private void validateTokenAuthorizationHeader(String authorizationHeader) {
        if (Strings.isBlank(authorizationHeader) || !authorizationHeader.startsWith(AUTHORIZATION_METHOD)) {
            log.error("유효하지 않는 Authorization header (input:{})", authorizationHeader);
            throw new AuthenticationException();
        }
    }

    private Long convertTokenToClaimsId(MethodParameter parameter, String token) {
        try {
            return tokenService.getById(token);
        } catch (final Exception e) {
            throw new AuthenticationException();
        }
    }


    private void validateInvalidJwtToken(MethodParameter parameter, String token) {
        if (!tokenService.verifyToken(token)) {
            log.error("유효하지 않는 Authorization 토큰(input:{})", token);
            log.error("{} - {}", parameter.getClass(), parameter.getMethod());
            throw new AuthenticationException();
        }
    }
}