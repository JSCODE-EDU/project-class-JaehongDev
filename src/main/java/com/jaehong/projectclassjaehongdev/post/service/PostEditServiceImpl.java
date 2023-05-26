package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.post.domain.PostEditor;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostEditRequest;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostEditResponse;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostEditServiceImpl implements PostEditService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 수정 로직 게시글 조회 사용자 조회
     * <p>
     * 사용자와 작성자 서로 비교 참인 경우 수정 거짓인 경우 에러
     *
     * @param postId
     * @param memberId
     * @param editRequest
     * @return
     */
    @Transactional
    @Override
    public PostEditResponse execute(Long postId, Long memberId, PostEditRequest editRequest) {
        // 2개를 분리해야 할까?
        var writer = memberRepository.findById(memberId).orElseThrow(() -> DomainExceptionCode.MEMBER_ID_DID_NOT_EXISTS.create(memberId));
        var previousPost = postRepository.findById(postId).orElseThrow(() -> DomainExceptionCode.POST_DID_NOT_EXISTS.create(postId));

        var updatedPost = postRepository.save(previousPost.update(PostEditor.builder()
                .writer(writer)
                .title(editRequest.getTitle())
                .content(editRequest.getContent())
                .build()));

        return PostEditResponse.builder()
                .id(updatedPost.getId())
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .createdAt(updatedPost.getCreatedAt())
                .build();
    }
}
