package com.jaehong.projectclassjaehongdev.member.service;


import com.jaehong.projectclassjaehongdev.global.config.PasswordEncode;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
public class SignUpDecorateServiceImpl implements SignUpService {
    private final SignUpService signUpService;
    private final PasswordEncode passwordEncode;

    @Override
    public void execute(SignUpRequest request) {
        signUpService.execute(
                SignUpRequest.builder()
                        .email(request.getEmail())
                        .password(passwordEncode.encode(request.getPassword()))
                        .build()
        );
    }
}
