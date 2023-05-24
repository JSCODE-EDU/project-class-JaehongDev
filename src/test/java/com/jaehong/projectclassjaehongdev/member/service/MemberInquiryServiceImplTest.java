package com.jaehong.projectclassjaehongdev.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.jaehong.projectclassjaehongdev.global.domain.DomainException;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
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
class MemberInquiryServiceImplTest {

    @InjectMocks
    private MemberInquiryServiceImpl memberInquiryService;
    @Mock
    private MemberRepository memberRepository;

    @Test
    void 존재하지_않는_회원_아이디의_경우_에러가_발생한다() {
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        var domainException = DomainExceptionCode.MEMBER_ID_DID_NOT_EXISTS.create(1L);
        assertThatThrownBy(() -> memberInquiryService.execute(1L)).isInstanceOf(DomainException.class)
                .satisfies((error) -> DomainExceptionValidator.validate(error, domainException));

    }

}