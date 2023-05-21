package com.jaehong.projectclassjaehongdev.global.resolver;

import com.jaehong.projectclassjaehongdev.global.authentication.annotation.MemberId;
import com.jaehong.projectclassjaehongdev.global.authentication.annotation.Secured;
import com.jaehong.projectclassjaehongdev.jwt.TokenService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class SignInArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenService tokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class)
                && parameter.hasParameterAnnotation(MemberId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getMethodAnnotation(Secured.class) == null) {
            throw new RuntimeException("@Secured 어노테이션을 붙여주세요.");
        }

        final var request = (HttpServletRequest) webRequest.getNativeRequest();
        final var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader.isBlank() || !authorizationHeader.startsWith("bearer")) {
            throw new IllegalStateException();
        }

        var token = authorizationHeader.substring(7);
        if (!tokenService.verifyToken(token)) {
            throw new RuntimeException(String.format("USER_ID를 가져오지 못했습니다. (%s - %s)", parameter.getClass(), parameter.getMethod()));
        }
        try {
            return Long.parseLong(tokenService.getSubject(token).toString());
        } catch (final NumberFormatException e) {
            throw new RuntimeException(String.format("USER_ID를 가져오지 못했습니다. (%s - %s)", parameter.getClass(), parameter.getMethod()));
        }
    }
}