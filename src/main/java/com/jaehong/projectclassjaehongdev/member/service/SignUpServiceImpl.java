package com.jaehong.projectclassjaehongdev.member.service;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.domain.Email;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignUpRequest;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void execute(SignUpRequest request) {
        memberRepository.findByEmail(Email.create(request.getEmail()))
                .ifPresent((member) -> {
                    throw DomainExceptionCode.MEMBER_EXISTS_EMAIL.create(member.getEmail());
                });

        var member = Member.create(request.getEmail(), request.getPassword());
        memberRepository.save(member);
    }
}
