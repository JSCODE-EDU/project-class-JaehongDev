package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostFindResponse;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostFindServiceImpl implements PostFindService {
    private final PostRepository postRepository;


    @Transactional(readOnly = true)
    @Override
    public PostFindResponse execute(Long postId) {
        // 조회 결과
        var postViewed = postRepository.findById(postId)
                .orElseThrow(() -> DomainExceptionCode.POST_DID_NOT_EXISTS.create(postId));

        return PostFindResponse.builder().id(postViewed.getId())
                .title(postViewed.getTitle())
                .content(postViewed.getContent())
                .createdAt(postViewed.getCreatedAt())
                .build();
    }
}
