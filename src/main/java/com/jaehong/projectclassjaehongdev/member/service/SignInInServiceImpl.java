package com.jaehong.projectclassjaehongdev.member.service;


import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.jwt.TokenService;
import com.jaehong.projectclassjaehongdev.member.domain.Email;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignInRequest;
import com.jaehong.projectclassjaehongdev.member.payload.response.SignInResponse;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInInServiceImpl implements SignInService {
    private final MemberRepository memberRepository;
    private final TokenService tokenService;


    @Override
    public SignInResponse execute(SignInRequest request) {
        var member = memberRepository.findByEmail(Email.create(request.getEmail()))
                .orElseThrow(DomainExceptionCode.AUTH_DID_NOT_CORRECT_LOGIN_INFORMATION::create);

        if (!member.comparePassword(request.getPassword())) {
            throw DomainExceptionCode.AUTH_DID_NOT_CORRECT_LOGIN_INFORMATION.create();
        }
        return SignInResponse.from(tokenService.issuedToken(member.getId(), 3600));
    }
}
