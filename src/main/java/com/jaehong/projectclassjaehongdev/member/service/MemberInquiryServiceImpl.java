package com.jaehong.projectclassjaehongdev.member.service;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.payload.response.MemberInquiryResponse;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberInquiryServiceImpl implements MemberInquiryService {

    private final MemberRepository memberRepository;

    @Override
    public MemberInquiryResponse execute(Long id) {
        var member = memberRepository.findById(id)
                .orElseThrow(() -> DomainExceptionCode.MEMBER_ID_DID_NOT_EXISTS.create(id));

        return MemberInquiryResponse.builder()
                .email(member.getEmail())
                .id(member.getId())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
