package com.jaehong.projectclassjaehongdev.member.integrate;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.domain.Email;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignUpRequest;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.utils.test.IntegrateTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;


@DisplayName("통합 테스트")
public class MemberApiIntegrateTest extends IntegrateTest {
    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("사용자의 회원가입에서")
    class SignUp {

        SignUpRequest signUpRequest;

        @BeforeEach
        void setUp() {
            this.signUpRequest = SignUpRequest.builder()
                    .email("email@email.com")
                    .password("password")
                    .build();
        }

        @Test
        void 중복되는_이메일을_생성하는_경우_오류가_발생한다() throws Exception {

            var domainException = DomainExceptionCode.MEMBER_EXISTS_EMAIL.create(signUpRequest.getEmail());
            memberRepository.save(Member.create(signUpRequest.getEmail(), signUpRequest.getPassword()));

            signUpRequestApi(signUpRequest)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.code").value(domainException.getCode()),
                            jsonPath("$.message").value(domainException.getMessage())
                    );
        }

        @Test
        void 성공적으로_완료된다() throws Exception {
            signUpRequestApi(signUpRequest)
                    .andExpect(status().isOk());
            var member = memberRepository.findByEmail(Email.create(signUpRequest.getEmail())).orElseThrow();

            assertAll(() -> assertThat(member.getEmail()).isEqualTo(signUpRequest.getEmail()),
                    () -> assertThat(member.getPassword()).isEqualTo(signUpRequest.getPassword()));


        }

        private ResultActions signUpRequestApi(SignUpRequest request) throws Exception {
            return mockMvc.perform(post("/api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print());
        }

    }
}
