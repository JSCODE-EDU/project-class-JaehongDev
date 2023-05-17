package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostDeleteServiceImpl implements PostDeleteService {
    private final PostRepository postRepository;

    @Transactional
    @Override
    public void execute(Long postId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> DomainExceptionCode.POST_DID_NOT_EXISTS.generateError(postId));
        postRepository.delete(post);
    }
}
