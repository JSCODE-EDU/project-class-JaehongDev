package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.config.domain.DomainExceptionCode;
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

    @Transactional
    @Override
    public PostEditResponse execute(Long postId, PostEditRequest editRequest) {
        var previousPost = postRepository.findById(postId)
                .orElseThrow(() -> DomainExceptionCode.POST_DID_NOT_EXISTS.generateError(postId));

        var updatedPost = postRepository.save(previousPost.updatePost(editRequest.getTitle(), editRequest.getContent()));

        return PostEditResponse.builder()
                .id(updatedPost.getId())
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .build();
    }
}
