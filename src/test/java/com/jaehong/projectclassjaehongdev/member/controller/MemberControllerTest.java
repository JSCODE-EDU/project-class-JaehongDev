package com.jaehong.projectclassjaehongdev.member.controller;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehong.projectclassjaehongdev.global.resolver.MemberIdArgumentResolver;
import com.jaehong.projectclassjaehongdev.jwt.TokenService;
import com.jaehong.projectclassjaehongdev.member.payload.response.MemberInquiryResponse;
import com.jaehong.projectclassjaehongdev.member.service.MemberInquiryService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest({MemberController.class, TokenService.class})
@DisplayName("회원 컨트롤러 단위 테스트")
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberIdArgumentResolver memberIdArgumentResolver;

    @MockBean
    private MemberInquiryService memberInquiryService;

    @Test
    void 개인정보를_조회할_수_있습니다() throws Exception {
        given(memberIdArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(1L);
        var response = MemberInquiryResponse.builder()
                .email("email@email.com")
                .id(1L)
                .createdAt(LocalDateTime.now()).build();
        given(memberInquiryService.execute(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/member/me"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(status().isOk(),
                        jsonPath("$.id").value(response.getId()),
                        jsonPath("$.email").value(response.getEmail()));

    }
}