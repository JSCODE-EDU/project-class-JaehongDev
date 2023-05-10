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
import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostCreateResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostEditResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostFindResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostsResponse;
import com.jaehong.projectclassjaehongdev.post.service.PostCreateService;
import com.jaehong.projectclassjaehongdev.post.service.PostDeleteService;
import com.jaehong.projectclassjaehongdev.post.service.PostEditService;
import com.jaehong.projectclassjaehongdev.post.service.PostFindService;
import com.jaehong.projectclassjaehongdev.post.service.PostsFindService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
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
    @MockBean
    PostsFindService postsFindService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;


    @Nested
    @DisplayName("[게시글 생성 api 단위 테스트] 게시글이")
    class PostCreateActionTest {
        @Test
        void 정상적으로_생성됩니다() throws Exception {
            var request = PostCreateRequest.builder()
                    .title("title")
                    .content("content")
                    .build();
            var response = PostCreateResponse.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();
            given(postCreateService.execute(request)).willReturn(response);

            requestNewPostApi(request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(response.getTitle()))
                    .andExpect(jsonPath("$.content").value(response.getContent()));
        }

        @Test
        void 잘못된_데이터의_경우_오류가_발생한다() throws Exception {
            var request = PostCreateRequest.builder().title("").content("content").build();

            var domainException = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY;
            given(postCreateService.execute(request)).willThrow(domainException.generateError());

            requestNewPostApi(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(domainException.getMessage()));
        }


        private ResultActions requestNewPostApi(PostCreateRequest request) throws Exception {
            return mockMvc.perform(post("/api/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print());

        }
    }

    @Nested
    @DisplayName("[게시글 수정 api 단위 테스트] 게시글이")
    class PostEditActionTest {
        @Test
        void 정상적으로_수정_됩니다() throws Exception {
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

            requestUpdatePostApi(request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(response.getId()))
                    .andExpect(jsonPath("$.title").value(response.getTitle()))
                    .andExpect(jsonPath("$.content").value(response.getContent()));
        }

        @Test
        void 없다면_수정할_수_없습니다() throws Exception {
            var request = PostEditRequest.builder()
                    .title("title")
                    .content("content")
                    .build();
            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS;
            given(postEditService.execute(1L, request)).willThrow(domainException.generateError(1L));

            requestUpdatePostApi(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(String.format(domainException.getMessage(), 1L)));
        }

        private ResultActions requestUpdatePostApi(PostEditRequest request) throws Exception {
            return mockMvc.perform(patch("/api/posts/{postId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("[게시글 삭제 api 단위 테스트] 게시글이")
    class PostDeleteActionTest {
        @Test
        void 정상적으로_삭제됩니다() throws Exception {
            requestDeletePost()
                    .andExpect(status().isCreated());
        }


        @Test
        void 없다면_삭제할_수_없습니다() throws Exception {
            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS;
            doThrow(domainException.generateError(1L)).when(postDeleteService).execute(1L);

            mockMvc.perform(delete("/api/posts/{postId}", 1L))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(String.format(domainException.getMessage(), 1L)));
        }

        private ResultActions requestDeletePost() throws Exception {
            return mockMvc.perform(delete("/api/posts/{postId}", 1L))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("[게시글 단건 조회 api 단위 테스트] 게시글이")
    class PostFindActionTest {
        @Test
        void 한건_조회합니다() throws Exception {
            var response = PostFindResponse.builder()
                    .id(1L)
                    .content("content")
                    .title("title")
                    .build();
            given(postFindService.execute(1L)).willReturn(response);

            requestFindPostApi()
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(response.getId()))
                    .andExpect(jsonPath("$.content").value(response.getContent()))
                    .andExpect(jsonPath("$.title").value(response.getTitle()));
        }

        @Test
        void 없다면_조회할_수_없습니다() throws Exception {

            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS;
            given(postFindService.execute(1L)).willThrow(domainException.generateError(1L));
            requestFindPostApi()
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(String.format(domainException.getMessage(), 1L)));
        }

        private ResultActions requestFindPostApi() throws Exception {
            return mockMvc.perform(get("/api/posts/{postId}", 1L))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("[게시글 다건 조회 api 단위 테스트] 게시글이")
    class PostsFindActionTest {
        @Test
        void 다건_조회됩니다() throws Exception {
            var data = LongStream.range(1, 11).mapToObj(index -> PostFindResponse.builder()
                    .id(index)
                    .title("title" + index)
                    .content("content" + index)
                    .build()
            ).collect(Collectors.toList());
            var response = PostsResponse.from(data);
            given(postsFindService.execute(PostSearch.builder().build())).willReturn(response);

            requestFindPostsApi()
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.posts.length()").value(10));
        }

        @Test
        void 비어있는_경우도_조회합니다() throws Exception {
            var response = PostsResponse.from(List.of());
            given(postsFindService.execute(PostSearch.builder().build())).willReturn(response);
            mockMvc.perform(get("/api/posts"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.posts.length()").value(0));
        }

        private ResultActions requestFindPostsApi() throws Exception {
            return mockMvc.perform(get("/api/posts"))
                    .andDo(print());
        }

    }
}