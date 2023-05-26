package com.jaehong.projectclassjaehongdev.post.integrate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.jwt.TokenService;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostCreateRequest;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostEditRequest;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostCreateResponse;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import com.jaehong.projectclassjaehongdev.utils.test.IntegrateTest;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.ResultActions;


@DisplayName("Post 통합 테스트")
public class PostApiIntegrateTest extends IntegrateTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TokenService tokenService;

    @Nested
    @DisplayName("게시글 생성 api에서")
    class PostCreateAction {
        String token;

        @BeforeEach
        void setup() {
            //create user
            var member = memberRepository.save(Member.create("email@email.com", "password"));
            this.token = tokenService.issuedToken(member.getId(), 3600);
        }

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
                    .andDo(document("post-create-success",
                            getDocumentRequest(),
                            getDocumentResponse(),
                            requestFields(
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목").attributes(
                                            Attributes.key("constraint").value("제목은 빈칸일 수 없으며 10자 이하만 가능합니다.")
                                    ),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용").attributes(
                                            Attributes.key("constraint").value("내용은 빈칸일 수 없으며 1000자 이하만 가능합니다.")
                                    )
                            ),
                            responseFields(
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간")
                            )
                    ))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value(response.getTitle()))
                    .andExpect(jsonPath("$.content").value(response.getContent()));
        }

        @Test
        void 제목이_없어서_생성하지_못합니다() throws Exception {
            var request = PostCreateRequest.builder().build();
            var domainException = DomainExceptionCode.POST_SHOULD_NOT_TITLE_EMPTY;
            requestPostCreateApi(request)
                    .andDo(document("post-create-fail",
                            getDocumentRequest(),
                            getDocumentResponse(),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("아이디"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("제목")
                            ))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(domainException.getMessage()));
        }

        private ResultActions requestPostCreateApi(PostCreateRequest request) throws Exception {
            return mockMvc.perform(post("/api/posts")
                            .header("Authorization", "Bearer " + token)
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
            var postEntity = postRepository.save(Post.create("title", "content", Member.create("emaiL@email.com", "password")));
            var newTitle = "new title";
            var newContent = "new content";
            var postEditeRequest = PostEditRequest.builder()
                    .title(newTitle)
                    .content(newContent)
                    .build();
            requestPostEditApi(postEditeRequest, postEntity.getId())
                    .andDo(document("post-edit",
                            getDocumentRequest(),
                            pathParameters(
                                    parameterWithName("id").description("게시글 id입니다")
                            ),
                            requestFields(
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목").attributes(
                                            Attributes.key("constraint").value("제목은 빈칸일 수 없으며 10자 이하만 가능합니다.")
                                    ),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용").attributes(
                                            Attributes.key("constraint").value("내용은 빈칸일 수 없으며 1000자 이하만 가능합니다.")
                                    )
                            ),
                            responseFields(
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간")
                            )
                    ))
                    .andExpect(jsonPath("$.title").value(newTitle))
                    .andExpect(jsonPath("$.content").value(newContent));
        }

        @Test
        void 아이디가_존재하지_않아서_수정을_실패합니다() throws Exception {
            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS.create(1L);
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
            var postEntity = postRepository.save(Post.create("title", "content", Member.create("emaiL@email.com", "password")));
            assertThat(postRepository.findAll().size()).isEqualTo(1);
            requestPostDeleteApi(postEntity.getId())
                    .andExpect(status().isNoContent())
                    .andDo(document("post-delete",
                            getDocumentRequest(),
                            pathParameters(
                                    parameterWithName("id").description("게시글 id입니다")
                            )
                    ));

            assertThat(postRepository.findAll().size()).isEqualTo(0);
        }

        @Test
        void 존재하지_않는_게시글은_삭제_실패합니다() throws Exception {
            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS.create(1L);

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

            var postEntity = postRepository.save(Post.create("title", "content", Member.create("emaiL@email.com", "password")));
            requestPostFindApi(postEntity.getId())
                    .andDo(document("post-inquiry",
                            getDocumentRequest(),
                            pathParameters(
                                    parameterWithName("id").description("게시글 id입니다")
                            ),
                            responseFields(
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간")
                            )
                    ))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.id").value(postEntity.getId()),
                            jsonPath("$.title").value(postEntity.getTitle()),
                            jsonPath("$.content").value(postEntity.getContent())
                    );
        }

        @Test
        void 존재하지_않는_게시글은_조회할_수_없습니다() throws Exception {
            var domainException = DomainExceptionCode.POST_DID_NOT_EXISTS.create(1L);

            requestPostFindApi(1L)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(domainException.getCode()))
                    .andExpect(jsonPath("$.message").value(domainException.getMessage()));
        }

        private ResultActions requestPostFindApi(Long id) throws Exception {
            return mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/{id}", id))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("게시글 다건 조회가")
    class PostsFindAction {
        @Test
        void 정상적으로_조회됩니다() throws Exception {
            var data = LongStream.range(1, 11)
                    .mapToObj(index -> Post.create("title" + index, "content" + index, Member.create("email@email.com", "password")))
                    .collect(Collectors.toList());
            postRepository.saveAll(data);
            requestPostsFindApi()
                    .andDo(document("post-search-all",
                            getDocumentRequest(),
                            getDocumentResponse(),
                            responseFields(
                                    beneathPath("posts"),
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간")))
                    )
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.posts.length()").value(10));
        }

        @Test
        void _100건이_넘는_결과는_100건만_조회됩니다() throws Exception {
            var data = LongStream.range(1, 111)
                    .mapToObj(index -> Post.create("title" + index, "content" + index, Member.create("email@email.com", "password")))
                    .collect(Collectors.toList());
            postRepository.saveAll(data);
            requestPostsFindApi("title")
                    .andDo(document("post-search-keyword",
                            getDocumentRequest(),
                            getDocumentResponse(),
                            requestParameters(
                                    parameterWithName("title").description("키워드인 제목입니다.").optional()
                            ),
                            responseFields(
                                    beneathPath("posts"),
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간")
                            ))
                    )
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.posts.length()").value(100));
        }

        private ResultActions requestPostsFindApi(String title) throws Exception {
            return mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts?title={title}", title))
                    .andDo(print());
        }

        private ResultActions requestPostsFindApi() throws Exception {
            return mockMvc.perform(get("/api/posts"))
                    .andDo(print());
        }
    }
}
