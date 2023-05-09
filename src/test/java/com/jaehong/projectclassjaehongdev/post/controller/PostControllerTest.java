package com.jaehong.projectclassjaehongdev.post.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehong.projectclassjaehongdev.config.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostCreateRequest;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostCreateResponse;
import com.jaehong.projectclassjaehongdev.post.service.PostCreateService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("post controller 단위 테스트")
@WebMvcTest(PostController.class)
class PostControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private PostCreateService postCreateService;

    @Nested
    @DisplayName("게시글 생성 api 단위 테스트")
    class PostCreateActionTest {
        @Test
        void 게시글_생성을_하면_정상적으로_응답이_떨어진다() throws Exception {
            var request = PostCreateRequest.builder()
                    .title("title")
                    .content("content")
                    .build();
            var response = PostCreateResponse.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();
            given(postCreateService.execute(request)).willReturn(response);

            mockMvc.perform(post("/api/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(response.getTitle()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(response.getContent()));
        }

        @Test
        void 잘못된_게시글을_생성하면_오류가_발생한다() throws Exception {
            var request = PostCreateRequest.builder()
                    .title("")
                    .content("content")
                    .build();

            var domainException = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY;
            given(postCreateService.execute(request)).willThrow(domainException.generateError());

            mockMvc.perform(post("/api/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(domainException.getCode()));
        }
    }

    @Nested
    @DisplayName("게시글 수정 api 단위 테스트")
    class PostEditActionTest {
        

    }
}