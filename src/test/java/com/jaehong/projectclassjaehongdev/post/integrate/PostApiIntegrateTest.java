package com.jaehong.projectclassjaehongdev.post.integrate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehong.projectclassjaehongdev.config.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostCreateRequest;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostEditRequest;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostCreateResponse;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Post 통합 테스트")
@Transactional
public class PostApiIntegrateTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PostRepository postRepository;


    @Nested
    @DisplayName("게시글 생성 api에서")
    class PostCreateAction {
        @Test
        void 성공적으로_생성됩니다() throws Exception {
            var request = PostCreateRequest.builder()
                    .title("title")
                    .content("content")
                    .build();
            var response = PostCreateResponse.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();
            requestPostCreateApi(request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(response.getTitle()))
                    .andExpect(jsonPath("$.content").value(response.getContent()));
        }

        @Test
        void 제목이_없어서_생성하지_못합니다() throws Exception {
            var request = PostCreateRequest.builder().build();
            var domainException = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY;
            requestPostCreateApi(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(domainException.getMessage()));
        }

        private ResultActions requestPostCreateApi(PostCreateRequest request) throws Exception {
            return mockMvc.perform(post("/api/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request)))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("게시글 수정 api에서")
    class PostEditAction {

        @Test
        void 정상적으로_수정이_됩니다() throws Exception {
            var postEntity = postRepository.save(Post.createNewPost("title", "content"));
            var newTitle = "new title";
            var newContent = "new content";
            var postEditeRequest = PostEditRequest.builder()
                    .title(newTitle)
                    .content(newContent)
                    .build();
            requestPostEditApi(postEditeRequest, postEntity.getId())
                    .andExpect(jsonPath("$.title").value(newTitle))
                    .andExpect(jsonPath("$.content").value(newContent));
        }

        @Test
        void 아이디가_존재하지_않아서_수정을_실패합니다() throws Exception {
            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS.generateError(1L);
            var postEditeRequest = PostEditRequest.builder()
                    .build();
            requestPostEditApi(postEditeRequest, 1L)
                    .andExpectAll(status().isBadRequest(),
                            jsonPath("$.code").value(domainException.getCode()),
                            jsonPath("$.message").value(domainException.getMessage())
                    );
        }

        private ResultActions requestPostEditApi(PostEditRequest request, Long id) throws Exception {
            return mockMvc.perform(patch("/api/posts/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print());
        }
    }


    @Nested
    @DisplayName("게시글 삭제")
    class PostDeleteAction {
        @Test
        void 정상적으로_삭제합니다() throws Exception {
            final var postEntity = postRepository.save(Post.createNewPost("title", "content"));
            assertThat(postRepository.findAll().size()).isEqualTo(1);
            requestPostDeleteApi(postEntity.getId())
                    .andExpect(status().isCreated());

            assertThat(postRepository.findAll().size()).isEqualTo(0);
        }

        @Test
        void 존재하지_않는_게시글은_삭제_실패합니다() throws Exception {
            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS.generateError(1L);

            requestPostDeleteApi(1L)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(domainException.getMessage()));
        }

        private ResultActions requestPostDeleteApi(Long id) throws Exception {
            return mockMvc.perform(delete("/api/posts/{id}", id))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("게시글 단건 조회가")
    class PostFindAction {
        @Test
        void 정상적으로_조회됩니다() throws Exception {
            final var postEntity = postRepository.save(Post.createNewPost("title", "content"));
            requestPostFindApi(postEntity.getId())
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.id").value(postEntity.getId()),
                            jsonPath("$.title").value(postEntity.getTitle()),
                            jsonPath("$.content").value(postEntity.getContent())
                    );
        }

        @Test
        void 존재하지_않는_게시글은_조회할_수_없습니다() throws Exception {
            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS.generateError(1L);

            requestPostFindApi(1L)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(domainException.getMessage()));
        }

        private ResultActions requestPostFindApi(Long id) throws Exception {
            return mockMvc.perform(get("/api/posts/{id}", id))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("게시글 다건 조회가")
    class PostsFindAction {
        @Test
        void 정상적으로_조회됩니다() throws Exception {
            var data = LongStream.range(1, 11)
                    .mapToObj(index -> Post.createNewPost("title" + index, "content" + index))
                    .collect(Collectors.toList());
            postRepository.saveAll(data);
            requestPostsFindApi("")
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.posts.length()").value(10));
        }

        @Test
        void _100건이_넘는_결과는_100건만_조회됩니다() throws Exception {
            var data = LongStream.range(1, 111)
                    .mapToObj(index -> Post.createNewPost("title" + index, "content" + index))
                    .collect(Collectors.toList());
            postRepository.saveAll(data);
            requestPostsFindApi("title")
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.posts.length()").value(100));
        }

        private ResultActions requestPostsFindApi(String title) throws Exception {
            return mockMvc.perform(get("/api/posts?title={title}", title))
                    .andDo(print());
        }
    }
}
