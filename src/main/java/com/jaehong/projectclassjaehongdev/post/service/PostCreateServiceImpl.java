package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostCreateRequest;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostCreateResponse;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCreateServiceImpl implements PostCreateService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public PostCreateResponse execute(PostCreateRequest request, Long memberId) {
        var writer = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
        var newPost = Post.create(request.getTitle(), request.getContent(), writer);
        var postEntity = postRepository.save(newPost);

        return PostCreateResponse.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .createdAt(postEntity.getCreatedAt())
                .build();
    }
}
