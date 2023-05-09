package com.jaehong.projectclassjaehongdev.post.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehong.projectclassjaehongdev.config.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostCreateRequest;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostEditRequest;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostCreateResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostEditResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostFindResponse;
import com.jaehong.projectclassjaehongdev.post.service.PostCreateService;
import com.jaehong.projectclassjaehongdev.post.service.PostDeleteService;
import com.jaehong.projectclassjaehongdev.post.service.PostEditService;
import com.jaehong.projectclassjaehongdev.post.service.PostFindService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("post controller 단위 테스트")
@WebMvcTest(PostController.class)
class PostControllerTest {
    @MockBean
    PostEditService postEditService;
    @MockBean
    PostCreateService postCreateService;
    @MockBean
    PostDeleteService postDeleteService;
    @MockBean
    PostFindService postFindService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;


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

            mockMvc.perform(post("/api/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(response.getTitle()))
                    .andExpect(jsonPath("$.content").value(response.getContent()));
        }

        @Test
        void 잘못된_게시글을_생성하면_오류가_발생한다() throws Exception {
            var request = PostCreateRequest.builder()
                    .title("")
                    .content("content")
                    .build();

            var domainException = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY;
            given(postCreateService.execute(request)).willThrow(domainException.generateError());

            mockMvc.perform(post("/api/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(domainException.getMessage()));
        }
    }

    @Nested
    @DisplayName("게시글 수정 api 단위 테스트")
    class PostEditActionTest {
        @Test
        void 수정이_정상적으로_처리됩니다() throws Exception {
            var request = PostEditRequest.builder()
                    .title("title")
                    .content("content")
                    .build();
            var response = PostEditResponse.builder()
                    .id(1L)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();
            given(postEditService.execute(1L, request)).willReturn(response);

            mockMvc.perform(patch("/api/posts/{postId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(response.getId()))
                    .andExpect(jsonPath("$.title").value(response.getTitle()))
                    .andExpect(jsonPath("$.content").value(response.getContent()));
        }

        @Test
        void 존재하지_않는_게시글을_수정할_수_없습니다() throws Exception {
            var request = PostEditRequest.builder()
                    .title("title")
                    .content("content")
                    .build();
            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS;
            given(postEditService.execute(1L, request)).willThrow(domainException.generateError(1L));

            mockMvc.perform(patch("/api/posts/{postId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(String.format(domainException.getMessage(), 1L)));
        }
    }

    @Nested
    @DisplayName("게시글 삭제 api 단위 테스트")
    class PostDeleteActionTest {
        @Test
        void 게시글을_삭제합니다() throws Exception {
            mockMvc.perform(delete("/api/posts/{postId}", 1L))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void 존재하지_않는_게시글은_에러_응답이_발생합니다() throws Exception {
            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS;
            doThrow(domainException.generateError(1L)).when(postDeleteService).execute(1L);

            mockMvc.perform(delete("/api/posts/{postId}", 1L))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(String.format(domainException.getMessage(), 1L)));
        }
    }

    @Nested
    @DisplayName("게시글 단건 조회 api 단위 테스트")
    class PostFindActionTest {
        @Test
        void 게시글을_하나_조회합니다() throws Exception {
            var response = PostFindResponse.builder()
                    .id(1L)
                    .content("content")
                    .title("title")
                    .build();
            BDDMockito.given(postFindService.execute(1L)).willReturn(response);

            mockMvc.perform(get("/api/posts/{postId}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(response.getId()))
                    .andExpect(jsonPath("$.content").value(response.getContent()))
                    .andExpect(jsonPath("$.title").value(response.getTitle()));
        }

        @Test
        void 존재하지_않는_게시글의_경우_error_응답() throws Exception {

            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS;
            given(postFindService.execute(1L)).willThrow(domainException.generateError(1L));
            mockMvc.perform(get("/api/posts/{postId}", 1L))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(String.format(domainException.getMessage(), 1L)));
        }


    }

}