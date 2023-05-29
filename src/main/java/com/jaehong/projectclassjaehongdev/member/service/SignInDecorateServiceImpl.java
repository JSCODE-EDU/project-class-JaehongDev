package com.jaehong.projectclassjaehongdev.member.service;

import com.jaehong.projectclassjaehongdev.global.config.PasswordEncode;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignInRequest;
import com.jaehong.projectclassjaehongdev.member.payload.response.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Primary
@Service
public class SignInDecorateServiceImpl implements SignInService {
    private final SignInService signInService;
    private final PasswordEncode passwordEncode;

    @Override
    public SignInResponse execute(SignInRequest request) {
        return this.signInService.execute(SignInRequest.builder()
                .email(request.getEmail())
                .password(passwordEncode.encode(passwordEncode.encode(request.getPassword())))
                .build()
        );
    }
}
