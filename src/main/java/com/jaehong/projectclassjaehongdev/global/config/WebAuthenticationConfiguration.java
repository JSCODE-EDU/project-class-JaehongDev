package com.jaehong.projectclassjaehongdev.global.config;


import com.jaehong.projectclassjaehongdev.global.resolver.MemberIdArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebAuthenticationConfiguration implements WebMvcConfigurer {
    private final MemberIdArgumentResolver memberIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberIdArgumentResolver);
    }
}
