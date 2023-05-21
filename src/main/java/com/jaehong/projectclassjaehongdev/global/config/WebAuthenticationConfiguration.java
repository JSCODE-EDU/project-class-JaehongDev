package com.jaehong.projectclassjaehongdev.global.config;


import com.jaehong.projectclassjaehongdev.global.resolver.SignInArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebAuthenticationConfiguration implements WebMvcConfigurer {
    private final SignInArgumentResolver signInArgumentResolver;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(signInArgumentResolver);
    }
}
