package com.jaehong.projectclassjaehongdev.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.domain.Email;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignUpRequest;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.utils.DomainExceptionValidator;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class SignUpServiceImplTest {

    @InjectMocks
    private SignUpServiceImpl signUpService;

    @Mock
    private MemberRepository memberRepository;

    //보류
    @Test
    void 중복되는_이메일이_생성되는_경우_에러가_발생합니다() {
        var email = "duplicate@email.com";
        var domainException = DomainExceptionCode.MEMBER_EXISTS_EMAIL.create(email);
        var request = SignUpRequest.builder().email(email)
                .password("password")
                .build();

        given(memberRepository.findByEmail(Email.create(email)))
                .willReturn(Optional.of(Member.create(email, request.getPassword())));

        assertThatThrownBy(() -> signUpService.execute(request))
                .isInstanceOf(DomainException.class)
                .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));

    }

}