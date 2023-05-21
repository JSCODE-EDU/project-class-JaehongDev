package com.jaehong.projectclassjaehongdev.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.jwt.TokenService;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignInRequest;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignUpRequest;
import com.jaehong.projectclassjaehongdev.member.payload.response.SignInResponse;
import com.jaehong.projectclassjaehongdev.member.service.SignInService;
import com.jaehong.projectclassjaehongdev.member.service.SignUpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("컨트롤러 단위 테스트")
@WebMvcTest({AuthController.class, TokenService.class})
class AuthControllerTest {

    @MockBean
    private SignUpService signUpService;
    @MockBean
    private SignInService signInService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;


    @Nested
    @DisplayName("회원가입에서")
    class SignUpTest {
        @Test
        void 중복되는_이메일이_입력되면_에러를_응답한다() throws Exception {
            var request = SignUpRequest.builder()
                    .email("email@email.com")
                    .password("password")
                    .build();
            var domainException = DomainExceptionCode.MEMBER_EXISTS_EMAIL.create(request.getEmail());

            doThrow(domainException).when(signUpService).execute(any());

            mockMvc.perform(post("/api/auth/signup").content(mapper.writeValueAsBytes(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpectAll(status().isBadRequest(),
                            jsonPath("$.code").value(domainException.getCode()),
                            jsonPath("$.message").value(domainException.getMessage())
                    );
        }
    }

    @Nested
    @DisplayName("로그인에서")
    class SignInTest {
        SignInRequest request = SignInRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();

        @Test
        void 로그인할_수_없는_요청이_발생한_경우_에러_응답이_발생한다() throws Exception {
            var domainException = DomainExceptionCode.AUTH_DID_NOT_CORRECT_LOGIN_INFORMATION.create();

            given(signInService.execute(request)).willThrow(domainException);

            requestSigInApi(request)
                    .andExpectAll(status().isBadRequest(),
                            jsonPath("$.code").value(domainException.getCode()),
                            jsonPath("$.message").value(domainException.getMessage())
                    );
        }

        @Test
        void 로그인에_성공한_경우() throws Exception {
            var response = SignInResponse.from("token");
            given(signInService.execute(request)).willReturn(response);
            requestSigInApi(request)
                    .andExpectAll(status().isOk(), jsonPath("$.token").value(response.getToken()));

        }

        private ResultActions requestSigInApi(SignInRequest request) throws Exception {
            return mockMvc.perform(post("/api/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print());

        }
    }

}