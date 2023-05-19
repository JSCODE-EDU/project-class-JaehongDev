package com.jaehong.projectclassjaehongdev.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignUpRequest;
import com.jaehong.projectclassjaehongdev.member.service.SignUpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("컨트롤러 단위 테스트")
@WebMvcTest(AuthController.class)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @MockBean
    private SignUpService signUpService;
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

}